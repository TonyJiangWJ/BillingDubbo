package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author jiangwj20966 2020/07/06
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundSoldInfo extends BaseVersionedEntity {

    private Long userId;
    private String fundCode;
    private String fundName;
    /**
     * 买入总支出
     */
    private BigDecimal purchaseCost;
    /**
     * 买入手续费
     */
    private BigDecimal purchaseFee;
    /**
     * 卖出收益
     */
    private BigDecimal soldIncome;
    /**
     * 卖出手续费
     */
    private BigDecimal soldFee;
    /**
     * 成本净值
     */
    private BigDecimal costValue;
    /**
     * 卖出单位净值
     */
    private BigDecimal soldValue;
    /**
     * 卖出份额
     */
    private BigDecimal soldAmount;
    /**
     * 卖出日期
     */
    private Date soldDate;
    /**
     * 卖出确认日期
     */
    private Date soldConfirmDate;

}