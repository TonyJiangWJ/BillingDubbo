package com.tony.billing.entity;


import com.tony.billing.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author by TonyJiang on 2017/6/13.
 */

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TagInfo extends BaseEntity {

    private String tagName;
    private Long userId;
}
