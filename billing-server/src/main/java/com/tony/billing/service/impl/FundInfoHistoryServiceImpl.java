package com.tony.billing.service.impl;

import com.tony.billing.dao.mapper.FundInfoHistoryMapper;
import com.tony.billing.entity.FundInfoHistory;
import com.tony.billing.service.api.FundInfoHistoryService;
import com.tony.billing.service.base.AbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author jiangwj20966 2020/09/03
*/
@Service
public class FundInfoHistoryServiceImpl extends AbstractServiceImpl<FundInfoHistory, FundInfoHistoryMapper> implements FundInfoHistoryService {

    @Autowired
    private FundInfoHistoryMapper fundInfoHistoryMapper;

}
