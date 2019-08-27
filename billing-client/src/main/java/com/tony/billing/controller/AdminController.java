package com.tony.billing.controller;

import com.tony.billing.entity.Admin;
import com.tony.billing.entity.ModifyAdmin;
import com.tony.billing.request.BaseRequest;
import com.tony.billing.request.admin.AdminLoginRequest;
import com.tony.billing.request.admin.AdminModifyPwdRequest;
import com.tony.billing.request.admin.AdminRegisterRequest;
import com.tony.billing.request.admin.ResetPasswordRequest;
import com.tony.billing.response.BaseResponse;
import com.tony.billing.service.api.AdminService;
import com.tony.billing.util.AuthUtil;
import com.tony.billing.util.CodeGeneratorUtil;
import com.tony.billing.util.ResponseUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author by TonyJiang on 2017/7/2.
 */
@RestController
@RequestMapping("/bootDemo")
public class AdminController extends BaseController {

    @Reference
    private AdminService adminService;

    @Resource
    private AuthUtil authUtil;

    @Value("${pwd.salt:springboot}")
    private String pwdSalt;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public BaseResponse login(@ModelAttribute("request") @Validated AdminLoginRequest request,
                              // 用于AOP获取IP地址等信息
                              HttpServletRequest httpServletRequest,
                              HttpServletResponse httpServletResponse) {
        BaseResponse response = new BaseResponse();
        try {
            Admin loginAdmin = new Admin();
            loginAdmin.setUserName(request.getUserName());
            loginAdmin.setPassword(request.getPassword());

            Admin admin = adminService.login(loginAdmin);
            if (admin != null) {
                authUtil.setCookieToken(admin.getTokenId(), httpServletResponse);
                ResponseUtil.success(response);
            } else {
                ResponseUtil.error(response);
            }
        } catch (Exception e) {
            logger.error("/user/login error", e);
            ResponseUtil.sysError(response);
        }
        return response;
    }

    @RequestMapping(value = "/user/register/put", method = RequestMethod.POST)
    public BaseResponse register(@ModelAttribute("request") @Validated AdminRegisterRequest registerRequest) {
        BaseResponse response = new BaseResponse();
        try {
            Admin admin = new Admin();
            admin.setUserName(registerRequest.getUserName());
            admin.setPassword(registerRequest.getPassword());
            admin.setCode(CodeGeneratorUtil.getCode(20));
            Long flag = 0L;
            if ((flag = adminService.register(admin)) > 0) {
                ResponseUtil.success(response);
            } else {
                ResponseUtil.error(response);
                if (flag.equals(-2L)) {
                    response.setMsg("账号已存在");
                }
            }
        } catch (Exception e) {
            logger.error("/user/register/put error", e);
            ResponseUtil.sysError(response);
        }
        return response;
    }

    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public BaseResponse logout(@ModelAttribute("request") BaseRequest request) {
        BaseResponse response = new BaseResponse();
        if (adminService.logout(request.getTokenId())) {
            return ResponseUtil.success(response);
        } else {
            return ResponseUtil.error(response);
        }
    }

    @RequestMapping(value = "/user/pwd/modify", method = RequestMethod.POST)
    public BaseResponse modifyPwd(@ModelAttribute("request") @Validated AdminModifyPwdRequest request) {
        BaseResponse response = new BaseResponse();
        ModifyAdmin modifyAdmin = new ModifyAdmin();

        modifyAdmin.setId(request.getUserId());
        modifyAdmin.setNewPassword(request.getNewPassword());
        modifyAdmin.setPassword(request.getOldPassword());
        if (adminService.modifyPwd(modifyAdmin)) {
            return ResponseUtil.success(response);
        } else {
            return ResponseUtil.error(response);
        }
    }

    @RequestMapping(value = "/login/status", method = RequestMethod.POST)
    public BaseResponse checkLoginStatus(@ModelAttribute("request") BaseRequest baseRequest) {
        return ResponseUtil.success();
    }

    @RequestMapping(value = "/user/pre/reset/password", method = RequestMethod.POST)
    public BaseResponse preResetPassword(@ModelAttribute("request") @Validated ResetPasswordRequest request) {
        if (adminService.preResetPwd(request.getUserName()) != null) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }
    }

    @RequestMapping(value = "/user/reset/password", method = RequestMethod.POST)
    public BaseResponse resetPassword(@ModelAttribute("request") @Validated ResetPasswordRequest request) {

        ModifyAdmin modifyAdmin = new ModifyAdmin();
        modifyAdmin.setNewPassword(request.getNewPassword());
        modifyAdmin.setTokenId(request.getToken());

        if (adminService.resetPwd(modifyAdmin)) {
            return ResponseUtil.success();
        } else {
            return ResponseUtil.error();
        }

    }

}
