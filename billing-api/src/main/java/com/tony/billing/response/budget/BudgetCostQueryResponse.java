package com.tony.billing.response.budget;

import com.tony.billing.dto.BudgetCostDTO;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author jiangwj20966 on 2017/7/13.
 */
public class BudgetCostQueryResponse extends BaseResponse {
    private List<BudgetCostDTO> budgetCostList;

    public List<BudgetCostDTO> getBudgetCostList() {
        return budgetCostList;
    }

    public void setBudgetCostList(List<BudgetCostDTO> budgetCostList) {
        this.budgetCostList = budgetCostList;
    }
}
