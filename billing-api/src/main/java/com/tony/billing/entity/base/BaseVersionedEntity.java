package com.tony.billing.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 带乐观锁的对象
 *
 * @author jiangwenjie 2019-03-18
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class BaseVersionedEntity extends BaseEntity {
    private Integer version;
}
