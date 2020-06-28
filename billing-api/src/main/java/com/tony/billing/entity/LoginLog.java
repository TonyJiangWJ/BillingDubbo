package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * @author: jiangwj20966 on 2018/1/25
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LoginLog extends BaseEntity {
    private String userName;
    private String loginResult;
    private String loginIp;
    private String code;
    private String msg;
}
