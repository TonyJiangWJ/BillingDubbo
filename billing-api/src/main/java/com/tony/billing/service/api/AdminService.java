package com.tony.billing.service.api;

import com.tony.billing.entity.Admin;
import com.tony.billing.entity.ModifyAdmin;
import org.apache.dubbo.config.annotation.Reference;

/**
 * @author by TonyJiang on 2017/5/18.
 */
public interface AdminService {

    Admin login(Admin admin);

    @Reference(retries = 0)
    Long register(Admin admin);

    boolean logout(String tokenId);

    boolean modifyPwd(ModifyAdmin admin);


    Admin preResetPwd(String userName);
    /**
     * oldPwd save the token
     * @param admin 修改密码的对象，保存新的密码，以及缓存token
     * @return
     */
    boolean resetPwd(ModifyAdmin admin);


}
