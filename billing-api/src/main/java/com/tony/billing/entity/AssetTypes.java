package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author TonyJiang 2018/6/21
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AssetTypes extends BaseVersionedEntity {
    private Long userId;
    private String parentCode;
    private String typeIdentify;
    private String typeCode;
    private String typeDesc;
}
