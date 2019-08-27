package com.tony.billing.util;

import com.tony.billing.response.BaseResponse;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
public class ResponseUtil {
    private static final String CODE_SUCCESS = "0001";
    private static final String CODE_PARAM_ERROR = "0002";
    private static final String CODE_SYS_ERROR = "0003";
    private static final String CODE_DATA_NOT_EXIST = "0004";
    private static final String CODE_ERROR = "0005";
    private static final String CODE_LOGIN_VERIFY = "0006";

    private static final String MSG_SUCCESS = "成功";
    private static final String MSG_PARAM_ERROR = "参数错误";
    private static final String MSG_SYS_ERROR = "系统异常";
    private static final String MSG_DATA_NOT_EXIST = "数据不存在";
    private static final String MSG_ERROR = "失败";
    private static final String MSG_LOGIN_VERIFY = "not login";

    public static BaseResponse sysError() {
        return sysError(new BaseResponse());
    }

    public static <T extends BaseResponse> T sysError(T response) {
        response.setCode(CODE_SYS_ERROR);
        response.setMsg(MSG_SYS_ERROR);
        return response;
    }

    public static BaseResponse success() {
        return success(new BaseResponse());
    }

    public static <T extends BaseResponse> T success(T response) {
        response.setCode(CODE_SUCCESS);
        response.setMsg(MSG_SUCCESS);
        return response;
    }

    public static BaseResponse paramError() {
        return paramError(new BaseResponse());
    }

    public static <T extends BaseResponse> T paramError(T response) {
        response.setCode(CODE_PARAM_ERROR);
        response.setMsg(MSG_PARAM_ERROR);
        return response;
    }

    public static BaseResponse dataNotExisting() {
        return dataNotExisting(new BaseResponse());
    }

    public static <T extends BaseResponse> T dataNotExisting(T response) {
        response.setMsg(MSG_DATA_NOT_EXIST);
        response.setCode(CODE_DATA_NOT_EXIST);
        return response;
    }

    public static BaseResponse error() {
        return error(new BaseResponse());
    }

    public static <T extends BaseResponse> T error(T response) {
        response.setMsg(MSG_ERROR);
        response.setCode(CODE_ERROR);
        return response;
    }

    public static BaseResponse loginError() {
        BaseResponse response = new BaseResponse();
        response.setCode(CODE_LOGIN_VERIFY);
        response.setMsg(MSG_LOGIN_VERIFY);
        return response;
    }
}
