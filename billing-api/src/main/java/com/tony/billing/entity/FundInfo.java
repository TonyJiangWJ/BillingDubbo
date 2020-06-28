package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jiangwenjie 2020/6/24
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundInfo extends BaseVersionedEntity {
    private Long userId;
    private String fundCode;
    private String fundName;
    private BigDecimal purchaseValue;
    private BigDecimal purchaseAmount;
    private BigDecimal purchaseCost;
    private BigDecimal purchaseFee;
    private Date purchaseDate;
    private Date confirmDate;
    private Integer inStore;
}
