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
public class FundHistoryValue extends BaseVersionedEntity {
    private String fundCode;
    private String fundName;
    private BigDecimal fundValue;
    private Date fundConfirmDate;
    private Date assessmentTime;
    private BigDecimal assessmentValue;
    private String assessmentIncreaseRate;
}
