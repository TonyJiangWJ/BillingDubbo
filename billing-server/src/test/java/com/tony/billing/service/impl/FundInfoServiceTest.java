package com.tony.billing.service.impl;

import com.tony.billing.request.fund.FundEnhanceRequest;
import com.tony.billing.service.BaseServiceNoTransactionalTest;
import com.tony.billing.service.api.FundInfoService;
import com.tony.billing.util.DateUtil;
import com.tony.billing.util.UserIdContainer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jiangwenjie 2020/9/3
 */
public class FundInfoServiceTest extends BaseServiceNoTransactionalTest {
    @Autowired
    private FundInfoService fundInfoService;

    @Test
    public void testEnhanceFund() {
        FundEnhanceRequest request = new FundEnhanceRequest();
        request.setUserId(2L);
        request.setFundCode("161725");
        request.setCurrentAmount("1940.28");
        request.setDateBefore(DateUtil.getDateFromString("2020-09-01", "yyyy-MM-dd"));
        UserIdContainer.setUserId(2L);
        Assert.assertTrue(fundInfoService.enhanceFund(request));
    }
}
