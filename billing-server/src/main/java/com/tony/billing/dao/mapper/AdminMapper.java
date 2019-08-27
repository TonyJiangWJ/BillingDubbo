package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.Admin;
import org.springframework.stereotype.Repository;

/**
 * @author by TonyJiang on 2017/7/12.
 */
@Repository
public interface AdminMapper extends AbstractMapper<Admin> {
    Long doLogin(Admin admin);

    Admin preLogin(Admin admin);

    Admin loginCheck(String tokenId);

    Admin queryByUserName(String userName);

    Long register(Admin admin);

    Long logout(Long userId);

    Long modifyPwd(Admin admin);

    Admin getAdminById(Long userId);
}
