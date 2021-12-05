package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractPageMapper;
import com.tony.billing.entity.CostRecord;
import com.tony.billing.entity.query.CostRecordQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author by TonyJiang on 2017/7/12.
 */
@Repository
public interface CostRecordMapper extends AbstractPageMapper<CostRecord, CostRecordQuery> {

    Integer batchInsert(@Param("insertList") List<CostRecord> insertList);

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

    /**
     * 批量切换删除状态
     * @param ids
     * @param userId
     * @param isDeleted
     * @return
     */
    Integer batchToggleDelete(@Param("ids") List<Long> ids, @Param("userId") Long userId, @Param("isDeleted") Integer isDeleted);

    /**
     * 批量切换是否显示
     * @param ids
     * @param userId
     * @param isHidden
     * @return
     */
    Integer batchToggleHidden(@Param("ids") List<Long> ids, @Param("userId") Long userId, @Param("isHidden") Integer isHidden);
}
