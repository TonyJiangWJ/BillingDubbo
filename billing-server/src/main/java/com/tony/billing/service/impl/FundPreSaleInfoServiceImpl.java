package com.tony.billing.service.impl;

import com.tony.billing.dao.mapper.FundPreSaleInfoMapper;
import com.tony.billing.entity.FundPreSaleInfo;
import com.tony.billing.entity.query.FundPreSaleInfoQuery;
import com.tony.billing.service.api.FundPreSaleInfoService;
import com.tony.billing.service.base.AbstractPageServiceImpl;
import org.apache.dubbo.config.annotation.Service;

/**
* @author jiangwj20966 2020/07/06
*/
@Service
public class FundPreSaleInfoServiceImpl extends AbstractPageServiceImpl<FundPreSaleInfo, FundPreSaleInfoQuery, FundPreSaleInfoMapper>
        implements FundPreSaleInfoService {

}
