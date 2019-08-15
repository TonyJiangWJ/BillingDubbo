package com.tony.billing.request.admin;

import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author by TonyJiang on 2017/7/8.
 */
public class AdminRegisterRequest extends BaseRequest {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
