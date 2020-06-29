package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tony.billing.constants.enums.EnumYesOrNo;
import com.tony.billing.dao.mapper.FundHistoryValueMapper;
import com.tony.billing.dao.mapper.FundInfoMapper;
import com.tony.billing.entity.FundHistoryValue;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.model.FundValueChangedModel;
import com.tony.billing.response.fund.DailyFundChangedResponse;
import com.tony.billing.response.fund.DailyFundHistoryValueResponse;
import com.tony.billing.service.api.FundHistoryValueService;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.service.base.AbstractService;
import com.tony.billing.util.ResponseUtil;
import com.tony.billing.util.UserIdContainer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author jiangwenjie 2020/6/28
 */
@Service
public class FundHistoryValueServiceImpl extends AbstractService<FundHistoryValue, FundHistoryValueMapper> implements FundHistoryValueService {

    @Autowired
    private FundInfoMapper fundInfoMapper;

    @Autowired
    private FundInfoService fundInfoService;

    @Value("${fund.value.query.url:http://fundgz.1234567.com.cn/js/%s.js}")
    private String fundValueQueryUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat("fund-history-%d").build());

    @Override
    public int updateFundHistoryValues() {
        List<FundInfo> fundsInDb = fundInfoMapper.getFundInfoDistinct();
        if (CollectionUtils.isNotEmpty(fundsInDb)) {
            fundsInDb.forEach(fundInfo -> threadPoolExecutor.execute(() -> updateFundInfo(fundInfo)));
            return fundsInDb.size();
        }
        return 0;
    }

    private void updateFundInfo(FundInfo fundInfo) {
        String queryUrl = String.format(fundValueQueryUrl, fundInfo.getFundCode());
        OkHttpClient client = new OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(queryUrl).build();
        int tryTime = 3;
        boolean succeed = false;
        // 尝试三次
        while (!succeed && tryTime-- > 0) {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String responseBodyStr = response.body().string();
                    responseBodyStr = responseBodyStr.replaceAll("jsonpgz\\(", "").replaceAll("\\);", "");
                    logger.debug("responseBody: {}", responseBodyStr);
                    JSONObject jsonObject = JSONObject.parseObject(responseBodyStr);
                    FundHistoryValue fundHistoryValue = new FundHistoryValue();
                    fundHistoryValue.setFundCode(fundInfo.getFundCode());
                    fundHistoryValue.setFundName(fundInfo.getFundName());
                    fundHistoryValue.setAssessmentIncreaseRate(jsonObject.getString("gszzl"));
                    fundHistoryValue.setAssessmentTime(jsonObject.getDate("gztime"));
                    fundHistoryValue.setAssessmentDate(jsonObject.getDate("gztime"));
                    fundHistoryValue.setAssessmentValue(new BigDecimal(jsonObject.getString("gsz")));
                    fundHistoryValue.setFundConfirmDate(jsonObject.getDate("jzrq"));
                    fundHistoryValue.setFundValue(new BigDecimal(jsonObject.getString("dwjz")));
                    logger.debug("get fund info:{}", JSON.toJSONString(fundHistoryValue));
                    insertIfNotExist(fundHistoryValue);
                    succeed = true;
                }
            } catch (IOException e) {
                logger.error("获取基金:" + fundInfo.getFundCode() + " 估值信息失败", e);
            }
        }
    }

    private void insertIfNotExist(FundHistoryValue fundHistoryValue) {
        if (mapper.checkIsHistoryValueExists(fundHistoryValue.getFundCode(), fundHistoryValue.getAssessmentTime()) <= 0) {
            super.insert(fundHistoryValue);
        }
    }

    @Override
    public DailyFundHistoryValueResponse getFundHistoryValuesByAssessmentDate(String assessmentDate) {
        Long userId = UserIdContainer.getUserId();
        List<FundInfo> userFunds = fundInfoMapper.getFundInfoDistinctByUser(userId);
        DailyFundHistoryValueResponse response = ResponseUtil.success(new DailyFundHistoryValueResponse());
        response.setAssessmentDate(assessmentDate);
        if (CollectionUtils.isNotEmpty(userFunds)) {

            Map<String, String> fundInfoMap = new HashMap<>(userFunds.size());
            for (FundInfo fundInfo : userFunds) {
                fundInfoMap.put(fundInfo.getFundCode(), fundInfo.getFundName());
            }
            response.setFundInfoMap(fundInfoMap);
            List<FundHistoryValue> dailyFundHistoryValues = mapper.getFundHistoriesByFundCodes(
                    userFunds.stream().map(FundInfo::getFundCode).collect(Collectors.toList()), assessmentDate);
            if (CollectionUtils.isNotEmpty(dailyFundHistoryValues)) {
                Map<String, List<FundHistoryValue>> fundHistoryValues = dailyFundHistoryValues
                        .parallelStream()
                        .collect(Collectors.groupingBy(FundHistoryValue::getFundCode));
                Map<String, List<String>> resultMap = fundHistoryValues.entrySet().stream().map(entry -> {
                    String fundCode = entry.getKey();
                    List<String> increaseRateList = entry.getValue().stream().map(FundHistoryValue::getAssessmentIncreaseRate).collect(Collectors.toList());
                    Collections.reverse(increaseRateList);
                    Map<String, List<String>> map = new HashMap<>(1);
                    map.put(fundCode, increaseRateList);
                    return map;
                }).reduce(new HashMap<>(userFunds.size()), (a, b) -> {
                    a.putAll(b);
                    return a;
                });
                response.setIncreaseRateMapping(resultMap);
                response.checkMinLength();
                // 计算总增长率
                generateTotalIncreaseRateInfo(response, userId);
                response.revertFundCodeAndName();
            }
        }
        return response;
    }

    private void generateTotalIncreaseRateInfo(DailyFundHistoryValueResponse response, Long userId) {
        List<FundInfo> userFundsWithValue = fundInfoService.listGroupedFundsByUserId(userId);
        List<String> totalIncreaseRateList = new ArrayList<>();
        logger.debug("用户持有总量信息：{}", JSON.toJSONString(userFundsWithValue));
        for (int i = 0; i < response.getLength(); i++) {
            totalIncreaseRateList.add(calculateResult(i, response.getIncreaseRateMapping(), userFundsWithValue));
        }
        response.getIncreaseRateMapping().put("总增长率", totalIncreaseRateList);
        response.getFundInfoMap().put("总增长率", "总增长率");
    }

    private String calculateResult(int index, Map<String, List<String>> increaseRateMapping, List<FundInfo> userFundsWithValue) {
        BigDecimal totalIncrease = BigDecimal.ZERO;
        BigDecimal totalValue = BigDecimal.ZERO;
        for (FundInfo fundInfo : userFundsWithValue) {
            List<String> rateList = increaseRateMapping.get(fundInfo.getFundCode());
            if (CollectionUtils.isNotEmpty(rateList)) {
                String rate = rateList.get(index);
                totalIncrease = totalIncrease.add(new BigDecimal(rate).multiply(fundInfo.getPurchaseAmount().multiply(fundInfo.getPurchaseValue())));
                totalValue = totalValue.add(fundInfo.getPurchaseValue().multiply(fundInfo.getPurchaseAmount()));
            } else {
                logger.warn("fund[{}]'s rate is not exist", fundInfo.getFundCode());
            }
        }
        return totalIncrease.divide(totalValue, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    @Override
    public DailyFundChangedResponse getFundChangedInfosByAssessmentDate(String assessmentDate) {
        DailyFundChangedResponse response = ResponseUtil.success(new DailyFundChangedResponse());
        Long userId = UserIdContainer.getUserId();
        List<FundInfo> userFunds = fundInfoService.listGroupedFundsByUserId(userId);
        FundInfo fundInfo = new FundInfo();
        fundInfo.setUserId(userId);
        fundInfo.setInStore(EnumYesOrNo.YES.val());
        List<FundInfo> userFundDetailList = fundInfoService.list(fundInfo);
        if (CollectionUtils.isNotEmpty(userFunds)) {
            List<FundHistoryValue> latestHistoryValues = mapper.getLatestFundHistoryValueByFundCodes(
                    userFunds.stream().map(FundInfo::getFundCode).collect(Collectors.toList()), assessmentDate
            );

            Map<String, FundHistoryValue> latestHistoryValueMap = new HashMap<>(latestHistoryValues.size());
            if (CollectionUtils.isNotEmpty(latestHistoryValues)) {
                latestHistoryValues.forEach(fundHistoryValue -> latestHistoryValueMap.put(fundHistoryValue.getFundCode(), fundHistoryValue));
                response.setSummaryFundInfos(getFundChangedInfos(userFunds, latestHistoryValueMap));
                response.setFundDetailInfos(getFundChangedInfos(userFundDetailList, latestHistoryValueMap));
                response.setAssessmentDate(assessmentDate);
                // 计算总额
                response.calculateIncreaseInfo();
            } else {
                response.setSummaryFundInfos(getFundChangedInfos(userFunds, latestHistoryValueMap));
                response.setFundDetailInfos(getFundChangedInfos(userFundDetailList, latestHistoryValueMap));
                response.setAssessmentDate(assessmentDate);
                // 计算总额
                response.calculateIncreaseInfo();
            }
        }
        return response;
    }

    private List<FundValueChangedModel> getFundChangedInfos(List<FundInfo> fundInfoList, final Map<String, FundHistoryValue> latestHistoryValueMap) {
        if (CollectionUtils.isNotEmpty(fundInfoList)) {
            return fundInfoList.parallelStream().map(fundInfo -> {
                FundValueChangedModel changedModel = new FundValueChangedModel();
                changedModel.setId(fundInfo.getId());
                changedModel.setFundCode(fundInfo.getFundCode());
                changedModel.setFundName(fundInfo.getFundName());
                changedModel.setFundPurchaseValue(fundInfo.getPurchaseValue().toString());
                changedModel.setPurchaseAmount(fundInfo.getPurchaseAmount().toString());
                changedModel.setPurchaseCost(fundInfo.getPurchaseCost().toString());
                changedModel.setPurchaseFee(fundInfo.getPurchaseFee().toString());
                changedModel.setPurchaseDate(fundInfo.getPurchaseDate());
                changedModel.setPurchaseConfirmDate(fundInfo.getConfirmDate());
                // 估算值
                FundHistoryValue fundHistoryValue = latestHistoryValueMap.get(fundInfo.getFundCode());
                if (fundHistoryValue != null) {
                    changedModel.setAssessmentTime(fundHistoryValue.getAssessmentTime());
                    changedModel.setAssessmentValue(fundHistoryValue.getAssessmentValue().toString());
                    changedModel.setValueConfirmDate(fundHistoryValue.getFundConfirmDate());
                    changedModel.setFundConfirmedValue(fundHistoryValue.getFundValue().toString());
                    // 当日估算净值变化量
                    changedModel.setTodayIncreaseRate(fundHistoryValue.getAssessmentIncreaseRate());
                    changedModel.setTodayIncrease(getIncreaseValue(fundInfo.getPurchaseValue(), fundInfo.getPurchaseAmount(), fundHistoryValue.getAssessmentIncreaseRate()).toString());
                    // 当日估算总变化量
                    changedModel.setAssessmentIncrease(calIncrease(fundInfo.getPurchaseValue(), fundInfo.getPurchaseAmount(), fundHistoryValue.getAssessmentValue()).toString());
                    changedModel.setAssessmentIncreaseRate(calRate(fundHistoryValue.getAssessmentValue(), fundInfo.getPurchaseValue()).toString());
                    // 当日已确认变化量
                    changedModel.setConfirmedIncrease(calIncrease(fundInfo.getPurchaseValue(), fundInfo.getPurchaseAmount(), fundHistoryValue.getFundValue()).toString());
                    changedModel.setConfirmedIncreaseRate(calRate(fundHistoryValue.getFundValue(), fundInfo.getPurchaseValue()).toString());
                } else {
                    // 当日估算净值变化量
                    changedModel.setTodayIncreaseRate("0");
                    changedModel.setTodayIncrease("0");
                    // 当日估算总变化量
                    changedModel.setAssessmentIncrease("0");
                    changedModel.setAssessmentIncreaseRate("0");
                    // 当日已确认变化量
                    changedModel.setConfirmedIncrease("0");
                    changedModel.setConfirmedIncreaseRate("0");
                }
                return changedModel;
            }).collect(Collectors.toList());
        }
        return null;
    }

    private BigDecimal getIncreaseValue(BigDecimal fundValue, BigDecimal fundAmount, String increaseRate) {
        return fundValue.multiply(fundAmount).multiply(new BigDecimal(increaseRate)).divide(BigDecimal.valueOf(100), BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算增长值
     *
     * @param fundValue
     * @param fundAmount
     * @param newConfirmedValue
     * @return
     */
    private BigDecimal calIncrease(BigDecimal fundValue, BigDecimal fundAmount, BigDecimal newConfirmedValue) {
        return newConfirmedValue.subtract(fundValue).multiply(fundAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 计算增长率
     *
     * @param newVal
     * @param oldVal
     * @return
     */
    private BigDecimal calRate(BigDecimal newVal, BigDecimal oldVal) {
        if (oldVal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return newVal.subtract(oldVal).multiply(BigDecimal.valueOf(100)).divide(oldVal, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
