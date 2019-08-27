package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.CostRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author by TonyJiang on 2017/7/12.
 */
@Repository
public interface CostRecordMapper extends AbstractMapper<CostRecord> {

    Integer batchInsert(@Param("insertList") List<CostRecord> insertList);

    List<CostRecord> page(Map<String, Object> params);

    Integer count(Map<String, Object> params);

    CostRecord findByTradeNo(@Param("tradeNo")String tradeNo, @Param("userId") Long userId);

    Integer checkTradeExistence(@Param("tradeNo") String tradeNo, @Param("userId") Long userId);

    Integer toggleDeleteStatus(Map<String, Object> params);

    Integer toggleHideStatus(Map<String, Object> params);

    Integer updateByTradeNo(CostRecord record);

    /**
     * 仅仅返回金额信息
     *
     * @param monthPrefix {format yyyy-MM}
     * @param userId 用户id
     * @param tagIds 关联所提供的标签id
     * @return
     */
    List<CostRecord> listByMonthAndTagIds(@Param("month") String monthPrefix,
                                          @Param("userId") Long userId,
                                          @Param("tagIds") List<Long> tagIds);

    /**
     * 仅仅返回金额信息
     *
     * @param monthPrefix {format yyyy-MM}
     * @param userId 用户id
     * @param tagIds 未关联所提供的标签id
     * @return
     */
    List<CostRecord> listByMonthAndExceptTagIds(@Param("month") String monthPrefix,
                                                @Param("userId") Long userId,
                                                @Param("tagIds") List<Long> tagIds);
}
