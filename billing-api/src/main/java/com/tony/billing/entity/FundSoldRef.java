package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
/**
 * @author jiangwj20966 2020/07/06
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundSoldRef extends BaseEntity {

    private Long fundId;
    private Long fundPreSoldId;

}