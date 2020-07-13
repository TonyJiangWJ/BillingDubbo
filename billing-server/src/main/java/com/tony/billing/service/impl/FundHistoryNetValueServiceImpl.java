package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.dao.mapper.FundHistoryNetValueMapper;
import com.tony.billing.dao.mapper.FundInfoMapper;
import com.tony.billing.entity.FundHistoryNetValue;
import com.tony.billing.entity.FundInfo;
import com.tony.billing.service.api.FundHistoryNetValueService;
import com.tony.billing.service.base.AbstractServiceImpl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jiangwj20966 2020/07/13
 */
@Service
public class FundHistoryNetValueServiceImpl extends AbstractServiceImpl<FundHistoryNetValue, FundHistoryNetValueMapper> implements FundHistoryNetValueService {

    @Autowired
    private FundInfoMapper fundInfoMapper;

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
            fundsInDb.parallelStream()
                    .map(fundInfo -> threadPoolExecutor.submit(() -> updateFundInfo(fundInfo)))
                    .map(future -> {
                        List<FundHistoryNetValue> list = null;
                        try {
                            list = future.get();
                        } catch (Exception e) {
                            // just drop the error
                            // e.printStackTrace();
                        }
                        return list;
                    }).filter(Objects::nonNull)
                    .forEach(resultList -> {
                        logger.info("total length: {}", resultList.size());
                        if (CollectionUtils.isNotEmpty(resultList)) {
                            int start = 0;
                            int divideSize = 300;
                            do {
                                int end = resultList.size() > start + divideSize ? start + divideSize : resultList.size() - 1;
                                mapper.batchInsert(resultList.subList(start, end));
                                start = end + 1;
                                logger.info("start index: {} totalSize:{}", start, resultList.size());
                            } while (start < resultList.size() - 1);
                        }
                    });
            return fundsInDb.size();
        }
        return 0;
    }

    private List<FundHistoryNetValue> updateFundInfo(FundInfo fundInfo) {
        String queryUrl = String.format(fundHistoryNetValueQueryUrl, fundInfo.getFundCode(), System.currentTimeMillis());
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
                                LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(netWorthObj.getLong("x")), TimeConstants.CHINA_ZONE);
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
                                netValue.setFundCode(fundInfo.getFundCode());
                                historyNetValueList.add(netValue);
                            }
                        }
                        logger.debug("get fund info length: {} values: {}", historyNetValueList.size(), JSON.toJSONString(historyNetValueList));
                        return historyNetValueList;
                    }
                }
            } catch (Exception e) {
                logger.error("获取基金:" + fundInfo.getFundCode() + " 估值信息失败", e);
            }
        }
        return Collections.emptyList();
    }

}
