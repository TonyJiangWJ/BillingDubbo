package com.tony.billing.response.budget;

import com.tony.billing.dto.BudgetDTO;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author jiangwj20966 on 2017/7/13.
 */
public class BudgetListResponse extends BaseResponse {
    private List<BudgetDTO> budgetList;

    public List<BudgetDTO> getBudgetList() {
        return budgetList;
    }

    public void setBudgetList(List<BudgetDTO> budgetList) {
        this.budgetList = budgetList;
    }
}
