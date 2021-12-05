package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author by TonyJiang on 2017/5/18.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Admin extends BaseVersionedEntity {
    private String tokenId;
    private Long tokenVerify;
    private String code;
    private String userName;
    private String email;
    private String password;
    /**
     * 密码版本
     */
    private Integer passwordVersion;
    private Date lastLogin;
}
