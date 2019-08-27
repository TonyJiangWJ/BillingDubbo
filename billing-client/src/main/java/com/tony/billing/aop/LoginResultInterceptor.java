package com.tony.billing.aop;

import com.alibaba.fastjson.JSON;
import com.tony.billing.entity.LoginLog;
import com.tony.billing.request.admin.AdminLoginRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.service.api.LoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class LoginResultInterceptor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private LoginLogService loginLogService;

    @AfterReturning(returning = "returnValue", pointcut = "execution(* com.tony.billing.controller.AdminController.login(..))")
    public void getResult(JoinPoint point, Object returnValue) {
        Object[] args = point.getArgs();
        AdminLoginRequest loginRequest = (AdminLoginRequest) args[0];
        HttpServletRequest httpServletRequest = (HttpServletRequest) args[1];
        BaseResponse response = (BaseResponse) returnValue;

        LoginLog loginLog = new LoginLog();

        loginLog.setLoginIp(getLoginIp(httpServletRequest));
        if (loginRequest.getUserName() != null) {
            loginLog.setUserName(loginRequest.getUserName());
        } else {
            loginLog.setUserName("未知");
        }
        loginLog.setLoginResult(JSON.toJSONString(response));
        loginLog.setCode(response.getCode());
        loginLog.setMsg(response.getMsg());
        Long id = loginLogService.addLog(loginLog);
        logger.info("{} 请求登录, 结果：{}", loginLog.getUserName(), loginLog.getLoginResult());
        logger.info("ip地址：{}", loginLog.getLoginIp());

        if (id != null) {
            logger.info("日志保存成功，id:{}", id);
        } else {
            logger.error("日志保存失败");
        }
    }

    private String getLoginIp(HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("X-Real-IP");
        if (StringUtils.isEmpty(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }
        return ip;
    }
}
