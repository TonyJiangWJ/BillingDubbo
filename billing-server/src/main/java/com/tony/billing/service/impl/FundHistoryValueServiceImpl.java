package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tony.billing.dao.mapper.FundHistoryValueMapper;
import com.tony.billing.dao.mapper.FundInfoMapper;
import com.tony.billing.entity.FundHistoryValue;
import com.tony.billing.entity.FundInfo;
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
    public DailyFundHistoryValueResponse getFundHistoryValuesByConfirmDate(String confirmDate) {
        Long userId = UserIdContainer.getUserId();
        List<FundInfo> userFunds = fundInfoMapper.getFundInfoDistinctByUser(userId);
        DailyFundHistoryValueResponse response = ResponseUtil.success(new DailyFundHistoryValueResponse());
        response.setConfirmDate(confirmDate);
        if (CollectionUtils.isNotEmpty(userFunds)) {

            Map<String, String> fundInfoMap = new HashMap<>(userFunds.size());
            for (FundInfo fundInfo : userFunds) {
                fundInfoMap.put(fundInfo.getFundCode(), fundInfo.getFundName());
            }
            response.setFundInfoMap(fundInfoMap);
            List<FundHistoryValue> dailyFundHistoryValues = mapper.getFundHistoriesByFundCodes(
                    userFunds.stream().map(FundInfo::getFundCode).collect(Collectors.toList()), confirmDate);
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

}
