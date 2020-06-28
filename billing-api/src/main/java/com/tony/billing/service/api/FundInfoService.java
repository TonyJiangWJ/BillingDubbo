package com.tony.billing.service.api;

import com.tony.billing.entity.FundInfo;

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
}
