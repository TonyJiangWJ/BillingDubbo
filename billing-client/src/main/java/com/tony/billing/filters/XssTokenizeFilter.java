package com.tony.billing.filters;

import com.tony.billing.filters.wapper.TokenServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 队请求数据进行XSS过滤，并修改request类型为TokenServletRequestWrapper，用于在AuthorityInterceptor中设置用户tokenId
 * @author by TonyJiang on 2017/7/2.
 */
@Slf4j
@Order(1)
@WebFilter(filterName = "filterDemo", urlPatterns = "/*")
public class XssTokenizeFilter extends OncePerRequestFilter {

    private static final String MULTIPART_CONTENT = "multipart/form-data";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        TokenServletRequestWrapper request;
        if (StringUtils.equals(httpServletRequest.getContentType(), MULTIPART_CONTENT)) {
            request = new TokenServletRequestWrapper(new CommonsMultipartResolver().resolveMultipart(httpServletRequest));
        } else {
            request = new TokenServletRequestWrapper(httpServletRequest);
        }
        filterChain.doFilter(request, httpServletResponse);
    }
}
