package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author jiangwj20966 2020/09/03
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundInfoHistory extends BaseVersionedEntity {

    /**
     * 原始基金id
     */
    private Long originId;
    /**
     * 变更类型：1、部分售出，2、增强
     */
    private Integer changeType;
    private Long userId;
    private String fundCode;
    private String fundName;
    /**
     * 买入单位净值
     */
    private BigDecimal purchaseValue;
    /**
     * 确认买入份额
     */
    private BigDecimal purchaseAmount;
    /**
     * 买入总支出
     */
    private BigDecimal purchaseCost;
    /**
     * 买入手续费
     */
    private BigDecimal purchaseFee;
    /**
     * 买入日期
     */
    private Date purchaseDate;
    /**
     * 确认日期
     */
    private Date confirmDate;
    /**
     * 是否依旧持有
     */
    private Integer inStore;

}