package com.tony.billing.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jiangwenjie 2020/7/15
 */
@Data
@ToString
@EqualsAndHashCode
public class FundPreSaleModel implements Serializable {
    private Long id;
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
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date soldDate;
    /**
     * 是否已确认卖出
     */
    private Integer converted;
    private Integer version;
}
