package com.tony.billing.response;

import java.io.Serializable;

/**
 * @author jiangwj20966 on 2017/6/2.
 */
public class BaseResponse implements Serializable {
    private String msg;
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
