package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 资产
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Asset extends BaseVersionedEntity {
    private Long userId;
    private String name;
    private String extName;
    private Long type;
    private String available;
    private Long amount;

}
