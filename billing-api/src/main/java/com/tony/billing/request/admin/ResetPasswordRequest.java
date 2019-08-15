package com.tony.billing.request.admin;

import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author jiangwenjie 2019-02-02
 */
public class ResetPasswordRequest extends BaseRequest {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String token;
    @NotEmpty
    private String newPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
