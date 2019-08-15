package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author jiangwenjie 2019-03-13
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TagBudgetRef extends BaseEntity {
    private Long budgetId;
    private Long tagId;
}
