package com.tony.billing.service.impl;

import com.alibaba.fastjson.JSON;
import com.tony.billing.dao.mapper.FundPreSaleInfoMapper;
import com.tony.billing.entity.FundPreSaleInfo;
import com.tony.billing.service.BaseServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jiangwenjie 2021/2/8
 */
public class PreSaleServiceTest extends BaseServiceTest {

    @Autowired
    private FundPreSaleInfoMapper fundPreSaleInfoMapper;

    private Long userId;

    @Before
    public void setup() {
        userId = 2L;
    }

    @Test
    public void testListSoldFundInfos() {
        FundPreSaleInfo saleInfo = new FundPreSaleInfo();
        saleInfo.setUserId(userId);
        List<FundPreSaleInfo> preSoldList = fundPreSaleInfoMapper.list(saleInfo);
        logger.info("saleInfo: {}", JSON.toJSONString(preSoldList));

        BigDecimal totalIncome = preSoldList.stream().map(FundPreSaleInfo::getAssessmentSoldIncome).reduce(BigDecimal::add).get();
        BigDecimal totalCost = preSoldList.stream().map(FundPreSaleInfo::getPurchaseCost).reduce(BigDecimal::add).get();
        BigDecimal totalSoldFee = preSoldList.stream().map(FundPreSaleInfo::getAssessmentSoldFee).reduce(BigDecimal::add).get();
        logger.info("总收入：{}", totalIncome);
        logger.info("总支出：{}", totalCost);
        logger.info("总卖出手续费：{}", totalSoldFee);
        logger.info("总收益：{}", totalIncome.subtract(totalSoldFee).subtract(totalCost));
    }
}
