package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author by TonyJiang on 2017/7/5.
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Budget extends BaseVersionedEntity {
    private Long userId;
    private String budgetName;
    private Long budgetMoney;
    private String belongYear;
    private Integer belongMonth;
}
