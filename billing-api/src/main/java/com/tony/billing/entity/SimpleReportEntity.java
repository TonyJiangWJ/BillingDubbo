package com.tony.billing.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author by TonyJiang on 2017/6/11.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
public class SimpleReportEntity implements Serializable {
    private String month;
    private Long totalCost;
    private Long totalIncome;

    public SimpleReportEntity() {
    }

    public SimpleReportEntity(String month, Long totalCost, Long totalIncome) {
        this.month = month;
        this.totalCost = totalCost;
        this.totalIncome = totalIncome;
    }
}
