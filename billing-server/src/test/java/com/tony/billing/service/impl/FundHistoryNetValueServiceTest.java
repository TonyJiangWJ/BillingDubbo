package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.tony.billing.service.BaseServiceNoTransactionalTest;
import com.tony.billing.service.api.FundHistoryNetValueService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author jiangwenjie 2020/7/13
 */
@Slf4j
public class FundHistoryNetValueServiceTest extends BaseServiceNoTransactionalTest {
    @Autowired
    private FundHistoryNetValueService fundHistoryNetValueService;

    @Test
    public void testHistoryNetValue() throws Exception {
        log.info("update funds: {}", fundHistoryNetValueService.updateHistoryNetValues());
        Thread.sleep(200000);
        log.info("wait done!");
    }

    @Test
    public void testGetHistoryNetValues() {
        log.info("历史数据：{}", JSON.toJSONString(fundHistoryNetValueService.getHistoryNetValuesByFundCode("260108", null, null)));
    }
}
