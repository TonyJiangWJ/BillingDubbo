package com.tony.billing.service.impl;

import com.google.common.base.Preconditions;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.constants.enums.EnumMailTemplateName;
import com.tony.billing.dao.mapper.AdminMapper;
import com.tony.billing.entity.Admin;
import com.tony.billing.entity.ModifyAdmin;
import com.tony.billing.exceptions.BaseBusinessException;
import com.tony.billing.service.api.AdminService;
import com.tony.billing.service.api.EmailService;
import com.tony.billing.service.base.AbstractServiceImpl;
import com.tony.billing.util.RSAUtil;
import com.tony.billing.util.RedisUtils;
import com.tony.billing.util.ShaSignHelper;
import com.tony.billing.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author by TonyJiang on 2017/5/18.
 */
@Service
public class AdminServiceImpl extends AbstractServiceImpl<Admin, AdminMapper> implements AdminService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource
    private RedisUtils redisUtils;
    @Resource
    private EmailService emailService;

    @Value("${system.host.reset.password}")
    private String resetPwdUrl;
    /**
     * 会话有效期为1天
     */
    private final Long VERIFY_TIME = 3600 * 24 * 1000L;

    @Resource
    private RSAUtil rsaUtil;

    @Value("${pwd.salt:springboot}")
    private String pwdSalt;


    @Override
    public Admin login(Admin admin) {
        admin.setPassword(sha256(rsaUtil.decrypt(admin.getPassword())));
        if (admin.getPassword() == null) {
            logger.error("password error");
            return null;
        }
        Admin checkUser = mapper.preLogin(admin);
        if (checkUser != null) {
            redisUtils.del(checkUser.getTokenId());
            checkUser.setTokenId(TokenUtil.getToken(checkUser.getCode(), checkUser.getUserName(), checkUser.getPassword()));
            checkUser.setTokenVerify(VERIFY_TIME);
            checkUser.setLastLogin(new Date());
            if (mapper.doLogin(checkUser) > 0) {
                redisUtils.set(checkUser.getTokenId(), deleteSecret(checkUser), VERIFY_TIME / 1000);
                return checkUser;
            }
        }
        return null;
    }


    @Override
    public Long register(Admin admin) {
        if (null == mapper.queryByUserName(admin.getUserName())) {
            admin.setCreateTime(new Date());
            admin.setModifyTime(admin.getCreateTime());
            admin.setVersion(1);
            admin.setPassword(sha256(rsaUtil.decrypt(admin.getPassword())));
            if (admin.getPassword() == null) {
                logger.error("password error");
                return -1L;
            }
            admin.setIsDeleted(EnumDeleted.NOT_DELETED.val());
            if (mapper.register(admin) > 0) {
                return admin.getId();
            } else {
                return -1L;
            }
        }
        return -2L;
    }

    @Override
    public boolean logout(String tokenId) {
        return redisUtils.del(tokenId);
    }

    @Override
    public boolean modifyPwd(ModifyAdmin admin) {
        Preconditions.checkNotNull(admin.getId(), "用户id不能为空");
        // 现将密码进行加解密处理
        admin.setNewPassword(sha256(rsaUtil.decrypt(admin.getNewPassword())));
        admin.setPassword(sha256(rsaUtil.decrypt(admin.getPassword())));
        if (admin.getNewPassword() == null) {
            return false;
        }
        Admin stored = mapper.getAdminById(admin.getId());
        if (stored != null && StringUtils.equals(stored.getPassword(), admin.getPassword())) {
            stored.setPassword(admin.getNewPassword());
            return mapper.modifyPwd(stored) > 0;
        }
        logger.error("用户：{} 修改密码，旧密码不正确", admin.getId());
        return false;
    }

    @Override
    public Admin preResetPwd(String userName) {
        Admin user = mapper.queryByUserName(userName);
        if (user != null) {
            String email = user.getEmail();
            if (StringUtils.isNotEmpty(email)) {
                String token = sha256(UUID.randomUUID().toString());
                user = deleteSecret(user);
                redisUtils.set(token, deleteSecret(user), 3600);
                user.setTokenId(token);
                // TODO send reset email
                Map<String, Object> contents = new HashMap<>();
                contents.put("title", "重置密码");
                contents.put("typeDesc", "重置密码");
                contents.put("resetLink", resetPwdUrl + "?token=" + token);
                try {
                    emailService.sendThymeleafMail(email, "用户重置密码", contents, EnumMailTemplateName.RESET_PWD_MAIL.getTemplateName());
                } catch (MessagingException e) {
                    throw new BaseBusinessException("发送重置邮件失败");
                }
                return user;
            }
        }
        throw new BaseBusinessException("用户名不存在, 或者未绑定邮箱");
    }

    @Override
    public boolean resetPwd(ModifyAdmin admin) {
        Preconditions.checkNotNull(admin.getNewPassword(), "新密码不能为空");
        // 密码预处理
        admin.setNewPassword(sha256(rsaUtil.decrypt(admin.getNewPassword())));
        String token = admin.getTokenId();
        Optional<Admin> optional = redisUtils.get(token, Admin.class);
        if (optional.isPresent()) {
            Admin cachedUser = optional.get();
            cachedUser.setPassword(admin.getNewPassword());
            if (mapper.modifyPwd(cachedUser) > 0) {
                // 密码修改完毕之后将缓存删除
                redisUtils.del(token);
                return true;
            }
        } else {
            throw new BaseBusinessException("token无效，请重新申请重置密码");
        }
        logger.error("重置密码失败");
        return false;
    }

    private Admin deleteSecret(Admin admin) {
        Admin admin1 = new Admin();
        admin1.setId(admin.getId());
        admin1.setTokenId(admin.getTokenId());
        admin1.setUserName(admin.getUserName());
        admin1.setLastLogin(admin.getLastLogin());
        return admin1;
    }

    private String sha256(String pwd) {
        if (pwd != null) {
            return ShaSignHelper.sign(pwd, pwdSalt);
        } else {
            return null;
        }
    }

}
