package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.FundInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jiangwenjie 2020/6/28
 */
public interface FundInfoMapper extends AbstractMapper<FundInfo> {

    /**
     * 获取库中所有的基金列表 按fundCode分组
     * @return
     */
    List<FundInfo> getFundInfoDistinct();

    /**
     * 获取用户当前持有的基金列表
     * @param userId
     * @return
     */
    List<FundInfo> getFundInfoDistinctByUser(@Param("userId") Long userId);

    /**
     * 根据基金id获取当前持有的基金列表
     * @param fundIds
     * @param userId
     * @return
     */
    List<FundInfo> listInStoreFunds(@Param("fundIds") List<Long> fundIds, @Param("userId") Long userId);
}
