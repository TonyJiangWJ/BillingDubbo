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
public class FundPreSaleInfo extends BaseVersionedEntity {

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
     * 预计卖出收益
     */
    private BigDecimal assessmentSoldIncome;
    /**
     * 预计卖出手续费
     */
    private BigDecimal assessmentSoldFee;
    /**
     * 预计卖出单位净值
     */
    private BigDecimal assessmentValue;
    /**
     * 成本净值
     */
    private BigDecimal costValue;
    /**
     * 卖出份额
     */
    private BigDecimal soldAmount;
    /**
     * 卖出日期
     */
    private Date soldDate;
    /**
     * 是否已确认卖出
     */
    private Integer converted;

}