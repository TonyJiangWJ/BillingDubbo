package com.tony.billing.service.api;

import com.tony.billing.entity.CostRecord;
import com.tony.billing.entity.PagerGrid;
import com.tony.billing.entity.query.CostRecordQuery;
import org.apache.dubbo.config.annotation.Reference;

import java.util.List;
import java.util.Map;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
public interface CostRecordService {
    List<CostRecord> find(CostRecord record);

    PagerGrid<CostRecord> page(PagerGrid<CostRecord> pagerGrid);

    CostRecord findByTradeNo(String tradeNo, Long userId);

    @Reference(retries = 0)
    Integer toggleDeleteStatus(Map<String, Object> params);

    @Reference(retries = 0)
    Integer toggleHideStatus(Map<String, Object> params);

    @Reference(retries = 0)
    Long orderPut(CostRecord record);

    Integer updateByTradeNo(CostRecord record);

    @Reference(retries = 0)
    Integer batchToggleDeleteStatus(List<Long> costIds, Integer isDeleted);

    @Reference(retries = 0)
    Integer batchToggleHiddenStatus(List<Long> costIds, Integer isHidden);
}
