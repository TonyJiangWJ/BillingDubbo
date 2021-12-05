package com.tony.billing.service.impl;

import com.google.common.base.Preconditions;
import com.tony.billing.dao.mapper.CostRecordMapper;
import com.tony.billing.entity.CostRecord;
import com.tony.billing.entity.query.CostRecordQuery;
import com.tony.billing.service.api.CostRecordService;
import com.tony.billing.service.base.AbstractPageServiceImpl;
import com.tony.billing.util.UserIdContainer;
import org.apache.dubbo.config.annotation.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
@Service
public class CostRecordServiceImpl extends AbstractPageServiceImpl<CostRecord, CostRecordQuery, CostRecordMapper> implements CostRecordService {

    @Override
    public List<CostRecord> find(CostRecord record) {
        return super.list(record);
    }


    @Override
    public CostRecord findByTradeNo(String tradeNo, Long userId) {
        return mapper.findByTradeNo(tradeNo, userId);
    }

    @Override
    public Integer toggleDeleteStatus(Map<String, Object> params) {
        return mapper.toggleDeleteStatus(params);
    }

    @Override
    public Integer toggleHideStatus(Map<String, Object> params) {
        return mapper.toggleHideStatus(params);
    }

    @Override
    public Long orderPut(CostRecord record) {
        if (mapper.findByTradeNo(record.getTradeNo(), record.getUserId()) != null) {
            return -1L;
        } else {
            return super.insert(record);
        }
    }

    @Override
    public Integer updateByTradeNo(CostRecord record) {
        Preconditions.checkNotNull(record.getVersion(), "version must not be null");
        record.setModifyTime(new Date());
        return mapper.updateByTradeNo(record);
    }

    @Override
    public Integer batchToggleDeleteStatus(List<Long> costIds, Integer isDeleted) {
        return mapper.batchToggleDelete(costIds, UserIdContainer.getUserId(), isDeleted);
    }

    @Override
    public Integer batchToggleHiddenStatus(List<Long> costIds, Integer isHidden) {
        return mapper.batchToggleHidden(costIds, UserIdContainer.getUserId(), isHidden);
    }
}
