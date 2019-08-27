package com.tony.billing.service.api;

import com.tony.billing.entity.CostRecord;
import com.tony.billing.entity.PagerGrid;
import com.tony.billing.entity.query.CostRecordQuery;

import java.util.List;
import java.util.Map;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
public interface CostRecordService {
    List<CostRecord> find(CostRecord record);

    PagerGrid<CostRecord> page(PagerGrid<CostRecord> pagerGrid);

    CostRecord findByTradeNo(String tradeNo, Long userId);

    Integer toggleDeleteStatus(Map<String, Object> params);

    Integer toggleHideStatus(Map<String, Object> params);

    Long orderPut(CostRecord record);

    Integer updateByTradeNo(CostRecord record);
}
