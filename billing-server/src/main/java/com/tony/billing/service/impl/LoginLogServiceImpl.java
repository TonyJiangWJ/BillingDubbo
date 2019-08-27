package com.tony.billing.service.impl;

import com.tony.billing.dao.mapper.LoginLogMapper;
import com.tony.billing.entity.LoginLog;
import com.tony.billing.service.api.LoginLogService;
import com.tony.billing.service.base.AbstractService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author: jiangwj20966 on 2018/1/25
 */
@Service
public class LoginLogServiceImpl extends AbstractService<LoginLog, LoginLogMapper> implements LoginLogService {

    @Override
    public Long addLog(LoginLog loginLog) {
        return super.insert(loginLog);
    }
}
