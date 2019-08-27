package com.tony.billing.interceptors;

import com.alibaba.fastjson.JSON;
import com.tony.billing.entity.Admin;
import com.tony.billing.filters.wapper.TokenServletRequestWrapper;
import com.tony.billing.util.AuthUtil;
import com.tony.billing.util.CookieUtil;
import com.tony.billing.util.RedisUtils;
import com.tony.billing.util.ResponseUtil;
import com.tony.billing.util.UserIdContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author by TonyJiang on 2017/7/1.
 * 权限校验
 */
@Component
public class AuthorityInterceptor implements HandlerInterceptor {

    @Resource
    private AuthUtil authUtil;

    @Resource
    private RedisUtils redisUtils;

    private Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        if (!isUserLogin(httpServletRequest)) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(ResponseUtil.loginError()));
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        logger.debug("请求处理结束：Access-Control-Allow-Origin:{}", httpServletResponse.getHeader("Access-Control-Allow-Origin"));
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        logger.debug("完全结束");
        UserIdContainer.removeUserId();
    }

    private boolean isUserLogin(HttpServletRequest request) throws Exception {
        Cookie tokenCok = CookieUtil.getCookie("token", request);
        if (tokenCok != null) {
            String tokenId = authUtil.getUserTokenId(tokenCok.getValue());
            Optional<Admin> store = redisUtils.get(tokenId, Admin.class);

            if (store.isPresent()) {
                Admin admin = store.get();
                UserIdContainer.setUserId(admin.getId());
                if (request instanceof TokenServletRequestWrapper) {
                    ((TokenServletRequestWrapper) request).addParameter("tokenId", tokenId);
                    ((TokenServletRequestWrapper) request).addParameter("userId", String.valueOf(admin.getId()));
                } else if (request instanceof StandardMultipartHttpServletRequest) {
                    ((TokenServletRequestWrapper) ((StandardMultipartHttpServletRequest) request).getRequest()).addParameter("tokenId", tokenId);
                    ((TokenServletRequestWrapper) ((StandardMultipartHttpServletRequest) request).getRequest()).addParameter("userId", String.valueOf(admin.getId()));
                }
                redisUtils.set(tokenId, admin, 3600 * 24);
                return true;
            }
        }
        return false;
    }
}
