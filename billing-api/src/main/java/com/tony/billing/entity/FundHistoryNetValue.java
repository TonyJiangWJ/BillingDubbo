package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
/**
 * @author jiangwj20966 2020/07/13
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundHistoryNetValue extends BaseEntity {

    /**
     * 基金编码
     */
    private String fundCode;
    /**
     * 单位净值
     */
    private BigDecimal fundNetValue;
    /**
     * 累积净值
     */
    private BigDecimal fundAcNetValue;
    /**
     * 净值确认日期
     */
    private String confirmDate;
    /**
     * 增长率
     */
    private BigDecimal increaseRate;

}