package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


/**
 * @author by TonyJiang on 2017/6/15.
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TagCostRef extends BaseEntity {
    private Long tagId;
    private Long costId;

}
