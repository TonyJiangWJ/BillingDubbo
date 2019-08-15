package com.tony.billing.request.budget;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;

/**
 * @author jiangwenjie 2019-03-25
 */
public class BudgetDeleteRequest extends BaseRequest {
    @OwnershipCheck(value = EnumOwnershipCheckTables.BUDGET)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
