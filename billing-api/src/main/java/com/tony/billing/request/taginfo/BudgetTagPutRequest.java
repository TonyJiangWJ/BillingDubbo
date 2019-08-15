package com.tony.billing.request.taginfo;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author jiangwenjie 2019-03-25
 */
public class BudgetTagPutRequest extends BaseRequest {

    @NotNull
    @OwnershipCheck(value = EnumOwnershipCheckTables.TAG_INFO)
    private Long tagId;
    @NotNull
    @OwnershipCheck(value = EnumOwnershipCheckTables.BUDGET)
    private Long budgetId;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }
}
