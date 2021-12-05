package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.dao.mapper.FundHistoryNetValueMapper;
import com.tony.billing.dao.mapper.FundInfoMapper;
import com.tony.billing.dao.mapper.FundPreSaleInfoMapper;
import com.tony.billing.entity.FundHistoryNetValue;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.response.fund.FundHistoryNetValueResponse;
import com.tony.billing.service.api.FundHistoryNetValueService;
import com.tony.billing.service.base.AbstractServiceImpl;
import com.tony.billing.util.DateUtil;
import com.tony.billing.util.ResponseUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author jiangwj20966 2020/07/13
 */
@Service
public class FundHistoryNetValueServiceImpl extends AbstractServiceImpl<FundHistoryNetValue, FundHistoryNetValueMapper> implements FundHistoryNetValueService {

    @Autowired
    private FundInfoMapper fundInfoMapper;
    @Autowired
    private FundPreSaleInfoMapper fundPreSaleInfoMapper;

    @Value("${fund.history.net.value.query.url:http://fund.eastmoney.com/pingzhongdata/%s.js?v=%s}")
    private String fundHistoryNetValueQueryUrl;

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 8, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), new ThreadFactoryBuilder().setNameFormat("fund-history-%d").build());
    private final Pattern DATA_NET_WORTH = Pattern.compile("var Data_netWorthTrend = ([^;]*);");
    private final Pattern DATA_AC_WORTH = Pattern.compile("var Data_ACWorthTrend = ([^;]*);");

    @Override
    public int updateHistoryNetValues() {
        List<FundInfo> fundsInDb = fundInfoMapper.getFundInfoDistinct();
        if (CollectionUtils.isNotEmpty(fundsInDb)) {
            fundsInDb.stream().map(FundInfo::getFundCode).forEach(this::updateFundHistoryNetValue);
            return fundsInDb.size();
        }
        return 0;
    }

    @Override
    public void updateFundHistoryNetValue(String fundCode) {
        threadPoolExecutor.execute(() -> {
            List<FundHistoryNetValue> fundHistoryNetValues = updateFundInfo(fundCode);
            if (CollectionUtils.isNotEmpty(fundHistoryNetValues)) {
                logger.info("total length: {}", fundHistoryNetValues.size());
                int start = 0;
                int divideSize = 300;
                do {
                    int end = Math.min(fundHistoryNetValues.size(), start + divideSize);
                    mapper.batchInsert(fundHistoryNetValues.subList(start, end));
                    start = end;
                    logger.info("start index: {} totalSize:{}", start, fundHistoryNetValues.size());
                } while (start < fundHistoryNetValues.size());
            }
        });
    }

    private List<FundHistoryNetValue> updateFundInfo(String fundCode) {
        String queryUrl = String.format(fundHistoryNetValueQueryUrl, fundCode, System.currentTimeMillis());
        OkHttpClient client = new OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(queryUrl).build();
        int tryTime = 3;
        // 尝试三次
        while (tryTime-- > 0) {
            try (
                    Response response = client.newCall(request).execute()
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBodyStr = response.body().string();
                    responseBodyStr = responseBodyStr.replaceAll("//\\*[^*]\\*//", "");
                    logger.debug("responseBody: {}", responseBodyStr);

                    List<FundHistoryNetValue> historyNetValueList = new ArrayList<>();
                    Matcher netWorthMatcher = DATA_NET_WORTH.matcher(responseBodyStr);
                    Matcher acWorthMatcher = DATA_AC_WORTH.matcher(responseBodyStr);
                    if (netWorthMatcher.find() && acWorthMatcher.find()) {
                        JSONArray historyNetWorthArray = JSON.parseArray(netWorthMatcher.group(1));
                        JSONArray historyAcWorthArray = JSON.parseArray(acWorthMatcher.group(1));
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        for (int i = 0; i < historyNetWorthArray.size(); i++) {
                            JSONObject netWorthObj = historyNetWorthArray.getJSONObject(i);
                            JSONArray acWorthArray = historyAcWorthArray.getJSONArray(i);
                            if (netWorthObj != null) {
                                LocalDateTime dateTime = LocalDateTime.ofInstant(
                                        Instant.ofEpochMilli(netWorthObj.getLong("x")),
                                        TimeConstants.CHINA_ZONE
                                );
                                if (dateTime.getYear() < LocalDateTime.now().getYear() - 1) {
                                    // 丢弃前年的数据
                                    continue;
                                }
                                FundHistoryNetValue netValue = new FundHistoryNetValue();
                                netValue.setConfirmDate(dateTimeFormatter.format(dateTime));
                                netValue.setFundNetValue(new BigDecimal(netWorthObj.getString("y")));
                                try {
                                    netValue.setFundAcNetValue(acWorthArray.getBigDecimal(1));
                                } catch (Exception e) {
                                    netValue.setFundAcNetValue(new BigDecimal(netWorthObj.getString("y")));
                                }
                                netValue.setIncreaseRate(new BigDecimal(netWorthObj.getString("equityReturn")));
                                netValue.setCreateTime(new Date());
                                netValue.setModifyTime(new Date());
                                netValue.setIsDeleted(EnumDeleted.NOT_DELETED.val());
                                netValue.setFundCode(fundCode);
                                historyNetValueList.add(netValue);
                            }
                        }
                        logger.debug("get fund info length: {} values: {}", historyNetValueList.size(), JSON.toJSONString(historyNetValueList));
                        return historyNetValueList;
                    }
                }
            } catch (Exception e) {
                logger.error("获取基金:" + fundCode + " 估值信息失败", e);
            }
        }
        return Collections.emptyList();
    }

    @Override
    public FundHistoryNetValueResponse getHistoryNetValuesByFundCode(String fundCode, String dateAfter, String dateBefore) {
        Preconditions.checkState(StringUtils.isNotEmpty(fundCode), "基金编码不能为空");
        if (StringUtils.isEmpty(dateAfter)) {
            LocalDate localDate = LocalDate.now();
            // 大约30个工作日
            localDate = localDate.plusDays(-43);
            dateAfter = DateUtil.formatDateTime(localDate, "yyyy-MM-dd");
        }
        if (StringUtils.isEmpty(dateBefore)) {
            dateBefore = DateUtil.formatDateTime(LocalDate.now(), "yyyy-MM-dd");
        }
        List<FundHistoryNetValue> historyNetValues = mapper.getHistoryNetValueInRange(dateAfter, dateBefore, fundCode);
        if (CollectionUtils.isNotEmpty(historyNetValues)) {
            FundHistoryNetValueResponse response = new FundHistoryNetValueResponse();
            response.setFundCode(fundCode);
            response.setHistoryNetValues(
                    historyNetValues.stream()
                            .map(fundHistory -> new HashMap<String, String>(4) {{
                                put(fundHistory.getConfirmDate(), fundHistory.getFundNetValue().toString());
                            }}).collect(Collectors.toList())
            );
            response.setPurchaseDates(fundInfoMapper.listPurchaseDatesInRange(dateAfter, dateBefore, fundCode));
            response.setSoldDates(fundPreSaleInfoMapper.listPurchaseDatesInRange(dateAfter, dateBefore, fundCode));
            return response;
        }
        return ResponseUtil.error(new FundHistoryNetValueResponse());
    }
}
