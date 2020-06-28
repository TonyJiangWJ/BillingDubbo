package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jiangwj20966 on 2017/6/2.
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CostRecord extends BaseVersionedEntity {
    private String tradeNo;
    private String orderNo;
    private String costCreateTime;
    private String paidTime;
    private String costModifyTime;
    private String location;
    private String orderType;
    private String target;
    private String goodsName;
    private Long money;
    private String inOutType;
    private String orderStatus;
    private Long serviceCost;
    private Long refundMoney;
    private String memo;
    private String tradeStatus;
    private Integer isHidden;
    private Long userId;
}
