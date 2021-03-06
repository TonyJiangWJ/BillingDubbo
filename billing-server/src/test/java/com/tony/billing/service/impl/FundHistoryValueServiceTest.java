package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.tony.billing.constants.timing.TimeConstants;
import com.tony.billing.service.BaseServiceTest;
import com.tony.billing.service.api.FundHistoryValueService;
import com.tony.billing.util.DateUtil;
import com.tony.billing.util.UserIdContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author jiangwenjie 2020/6/28
 */
@Slf4j
public class FundHistoryValueServiceTest extends BaseServiceTest {

    @Reference
    private FundHistoryValueService fundHistoryValueService;

    @Test
    public void testUpdate() throws InterruptedException {
        log.info("update funds: {}", fundHistoryValueService.updateFundHistoryValues());
        Thread.sleep(20000);
        log.info("wait done!");
    }

    @Test
    public void testFundChangeLog() {
        UserIdContainer.setUserId(2L);
        log.info("result:{}", JSON.toJSONString(fundHistoryValueService.getFundHistoryValuesByAssessmentDate("2020-07-11")));
    }

    @Test
    public void testFundSummary() {
        UserIdContainer.setUserId(2L);
        log.info("result:{}", JSON.toJSONString(fundHistoryValueService.getFundChangedInfosByAssessmentDate("2020-07-17")));
    }
}
