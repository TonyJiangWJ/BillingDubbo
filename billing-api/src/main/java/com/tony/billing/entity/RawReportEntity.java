package com.tony.billing.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author jiangwj20966 8/6/2018
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
public class RawReportEntity implements Serializable {
    private Long totalCost;
    private Long totalCostExceptDeleted;
    private Long totalCostExceptHidden;
    private Long totalCostExceptDeletedAndHidden;
    private Long totalCostHidden;
    private Long totalCostDeleted;
    private Long totalCostDeletedAndHidden;
    private Long totalIncome;
    private Long totalIncomeExceptDeleted;
    private Long totalIncomeExceptHidden;
    private Long totalIncomeExceptDeletedAndHidden;
    private Long totalIncomeHidden;
    private Long totalIncomeDeleted;
    private Long totalIncomeDeletedAndHidden;

    public RawReportEntity() {
        totalCost = 0L;
        totalCostExceptDeleted = 0L;
        totalCostExceptHidden = 0L;
        totalCostExceptDeletedAndHidden = 0L;
        totalCostHidden = 0L;
        totalCostDeleted = 0L;
        totalCostDeletedAndHidden = 0L;
        totalIncome = 0L;
        totalIncomeExceptDeleted = 0L;
        totalIncomeExceptHidden = 0L;
        totalIncomeExceptDeletedAndHidden = 0L;
        totalIncomeHidden = 0L;
        totalIncomeDeleted = 0L;
        totalIncomeDeletedAndHidden = 0L;
    }

    public void calculateAdditional() {
        totalCostExceptDeleted = totalCost - totalCostDeleted;
        totalCostExceptHidden = totalCost - totalCostHidden;


        totalIncomeExceptDeleted = totalIncome - totalIncomeDeleted;
        totalIncomeExceptHidden = totalIncome - totalIncomeHidden;

    }
}
