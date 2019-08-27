package com.tony.billing.dao.mapper;

import com.tony.billing.entity.RawReportEntity;
import com.tony.billing.entity.query.ReportEntityQuery;
import org.springframework.stereotype.Repository;

/**
 * @author by TonyJiang on 2017/7/12.
 */
@Repository
public interface CostReportMapper {
    RawReportEntity getReportTypeAmountByCondition(ReportEntityQuery query);
}
