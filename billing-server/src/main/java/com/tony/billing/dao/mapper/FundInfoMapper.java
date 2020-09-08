package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.FundInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/28
 */
@Repository
public interface FundInfoMapper extends AbstractMapper<FundInfo> {

    /**
     * 获取库中所有的基金列表 按fundCode分组
     *
     * @return
     */
    List<FundInfo> getFundInfoDistinct();

    /**
     * 获取用户当前持有的基金列表
     *
     * @param userId
     * @return
     */
    List<FundInfo> getFundInfoDistinctByUser(@Param("userId") Long userId);

    /**
     * 根据基金id获取当前持有的基金列表
     *
     * @param fundIds
     * @param userId
     * @return
     */
    List<FundInfo> listInStoreFunds(@Param("fundIds") List<Long> fundIds, @Param("userId") Long userId);

    /**
     * 获取用户已存在的基金列表
     *
     * @param userId
     * @param fundCodes
     * @return
     */
    List<FundInfo> listUserExistsFunds(@Param("userId") Long userId, @Param("fundCodes") List<String> fundCodes);

    /**
     * 批量新增
     *
     * @param forAddFunds
     * @return
     */
    int batchInsert(@Param("forAddFunds") List<FundInfo> forAddFunds);

    /**
     * 列出在某个时间点之前确认买入（confirmDate）的，且匹配查询条件的基金列表
     *
     * @param condition
     * @param beforeDate
     * @return
     */
    List<FundInfo> listFundsBefore(@Param("condition") FundInfo condition, @Param("beforeDate") Date beforeDate);
}
