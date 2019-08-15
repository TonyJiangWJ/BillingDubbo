package com.tony.billing.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jiangwj20966 8/6/2018
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
public class RawReportEntity {
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

    public void calculateAdditional() {
        totalCostExceptDeleted = totalCost - totalCostDeleted;
        totalCostExceptHidden = totalCost - totalCostHidden;


        totalIncomeExceptDeleted = totalIncome - totalIncomeDeleted;
        totalIncomeExceptHidden = totalIncome - totalIncomeHidden;

    }
}
