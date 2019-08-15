package com.tony.billing.dto;

import com.tony.billing.dto.base.BaseDTO;

/**
 * @author jiangwj20966 on 2017/7/13.
 */
public class BudgetCostDTO extends BaseDTO {
    private String totalCost;
    private String restMoney;
    private String budgetMoney;
    private String belongYearMonth;
    private String costExpHidden;
    private String costExpDelete;
    private String costClear;

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getRestMoney() {
        return restMoney;
    }

    public void setRestMoney(String restMoney) {
        this.restMoney = restMoney;
    }

    public String getBudgetMoney() {
        return budgetMoney;
    }

    public void setBudgetMoney(String budgetMoney) {
        this.budgetMoney = budgetMoney;
    }

    public String getBelongYearMonth() {
        return belongYearMonth;
    }

    public void setBelongYearMonth(String belongYearMonth) {
        this.belongYearMonth = belongYearMonth;
    }

    public String getCostExpHidden() {
        return costExpHidden;
    }

    public void setCostExpHidden(String costExpHidden) {
        this.costExpHidden = costExpHidden;
    }

    public String getCostExpDelete() {
        return costExpDelete;
    }

    public void setCostExpDelete(String costExpDelete) {
        this.costExpDelete = costExpDelete;
    }

    public String getCostClear() {
        return costClear;
    }

    public void setCostClear(String costClear) {
        this.costClear = costClear;
    }
}
