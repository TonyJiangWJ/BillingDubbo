package com.tony.billing.request.taginfo;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author jiangwenjie 2019-03-25
 */
public class BudgetTagAssignableListRequest extends BaseRequest {
    @NotNull
    @OwnershipCheck(EnumOwnershipCheckTables.BUDGET)
    private Long budgetId;

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }
}
