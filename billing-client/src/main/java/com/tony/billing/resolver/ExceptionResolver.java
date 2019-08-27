package com.tony.billing.resolver;

import com.alibaba.fastjson.JSON;
import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.util.ResponseUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author: jiangwj20966 on 2018/1/25
 */
@ControllerAdvice
public class ExceptionResolver extends AbstractHandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    @Override
    protected ModelAndView doResolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        logger.error("biz error:", e);
        BaseResponse baseResponse = ResponseUtil.sysError();
        if (e instanceof BaseBusinessException) {
            baseResponse.setMsg(e.getMessage());
        } else if (e instanceof BindException) {
            baseResponse = ResponseUtil.paramError();
            List<ObjectError> errorList = ((BindException) e).getAllErrors();
            if (CollectionUtils.isNotEmpty(errorList)) {
                errorList.forEach((error -> {
                    if (error instanceof FieldError) {
                        logger.error("[{}]参数错误：{}", ((FieldError) error).getField(), error.getDefaultMessage());
                    } else {
                        logger.error("参数错误：{}", error.getDefaultMessage());
                    }
                }));
            }
        }
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Cache-Control", "no-cache, must-revalidate");
        try {
            httpServletResponse.getWriter().write(JSON.toJSONString(baseResponse));
        } catch (IOException ioe) {
            logger.error("与客户端通讯异常：" + ioe.getMessage(), ioe);
        }

        return new ModelAndView();
    }
}
