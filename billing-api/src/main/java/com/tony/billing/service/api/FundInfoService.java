package com.tony.billing.service.api;

import com.tony.billing.entity.FundInfo;
import com.tony.billing.model.FundAddModel;
import com.tony.billing.model.FundExistenceCheck;
import org.apache.dubbo.config.annotation.Reference;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/28
 */
public interface FundInfoService {
    /**
     * 新增记录
     * @param fundInfo
     * @return
     */
    @Reference(retries = 0)
    Long insert(FundInfo fundInfo);

    boolean update(FundInfo fundInfo);

    List<FundInfo> list(FundInfo fundInfo);

    FundInfo getById(Long id);

    /**
     * 获取当前持有的基金列表
     * @param userId
     * @return
     */
    List<FundInfo> listGroupedFundsByUserId(Long userId);

    boolean deleteById(Long id);

    /**
     * 预售出基金
     * @param fundIds
     * @param soldFeeRate
     * @param assessmentDate
     * @return
     */
    @Reference(retries = 0)
    boolean preMarkFundsAsSold(List<Long> fundIds, BigDecimal soldFeeRate, String assessmentDate);

    List<FundExistenceCheck> checkFundsExistence(List<FundExistenceCheck> fundCheckList);

    @Reference(retries = 0)
    boolean batchAddFunds(List<FundAddModel> fundInfoList);
}
