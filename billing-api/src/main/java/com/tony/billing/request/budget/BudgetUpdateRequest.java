package com.tony.billing.request.budget;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseVersionedRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author jiangwenjie 2019-03-25
 */
public class BudgetUpdateRequest extends BaseVersionedRequest {

    @NotNull
    @OwnershipCheck(value = EnumOwnershipCheckTables.BUDGET)
    private Long id;
    private String name;
    @NotNull
    @Min(0)
    private Long amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
