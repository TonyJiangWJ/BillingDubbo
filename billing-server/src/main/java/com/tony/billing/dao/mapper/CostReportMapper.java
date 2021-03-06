package com.tony.billing.dao.mapper;

import com.tony.billing.entity.CostRecord;
import com.tony.billing.entity.RawReportEntity;
import com.tony.billing.entity.query.ConditionalReportEntityQuery;
import com.tony.billing.entity.query.ReportEntityQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author by TonyJiang on 2017/7/12.
 */
@Repository
public interface CostReportMapper {
    /**
     * 获取某个月的收入支出等等数据
     *
     * @param query
     * @return
     */
    RawReportEntity getReportTypeAmountByCondition(ReportEntityQuery query);

    /**
     * 获取符合条件的总金额
     *
     * @param query
     * @return
     */
    Long getReportAmountByCondition(ConditionalReportEntityQuery query);

    List<CostRecord> getAllCostInfoBetween(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("userId") Long userId);
}
