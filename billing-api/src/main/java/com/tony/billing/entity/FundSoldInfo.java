package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jiangwenjie 2020/6/28
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundSoldInfo extends BaseVersionedEntity {
    private Long userId;
    private String fundCode;
    private String fundName;
    private BigDecimal originValue;
    private BigDecimal soldValue;
    private BigDecimal soldAmount;
    private BigDecimal soldIncome;
    private BigDecimal soldFee;
    private Date soldDate;
    private Date soldConfirmDate;
}
