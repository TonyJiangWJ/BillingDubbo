package com.tony.billing.service.api;

import com.tony.billing.entity.FundInfo;
import com.tony.billing.model.FundAddModel;
import com.tony.billing.model.FundExistenceCheck;
import com.tony.billing.request.fund.FundEnhanceRequest;
import com.tony.billing.request.fund.FundPreSalePortionRequest;
import com.tony.billing.service.api.base.AbstractService;
import org.apache.dubbo.config.annotation.Reference;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/28
 */
public interface FundInfoService extends AbstractService<FundInfo> {

    /**
     * 获取当前持有的基金列表
     *
     * @param userId
     * @return
     */
    List<FundInfo> listGroupedFundsByUserId(Long userId);

    /**
     * 预售出基金
     *
     * @param fundIds
     * @param soldFeeRate
     * @param assessmentDate
     * @return
     */
    @Reference(retries = 0)
    boolean preMarkFundsAsSold(List<Long> fundIds, BigDecimal soldFeeRate, String assessmentDate);

    /**
     * 校验并返回数据库中已存在的基金信息列表
     * fundCode&purchaseAmount&confirmDate
     *
     * @param fundCheckList
     * @return
     */
    List<FundExistenceCheck> checkFundsExistence(List<FundExistenceCheck> fundCheckList);

    /**
     * 批量新增基金信息
     *
     * @param fundInfoList
     * @return
     */
    @Reference(retries = 0)
    boolean batchAddFunds(List<FundAddModel> fundInfoList);

    /**
     * 卖出部分
     * @param request
     * @return
     */
    @Reference(retries = 0)
    boolean preSalePortion(FundPreSalePortionRequest request);

    /**
     * 基金增强
     *
     * @param request
     * @return
     */
    @Reference(retries = 0)
    boolean enhanceFund(FundEnhanceRequest request);
}
