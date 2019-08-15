package com.tony.billing.request.liability;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseVersionedRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * </p>
 * <li></li>
 *
 * @author jiangwj20966 2018/2/22
 */
public class LiabilityUpdateRequest extends BaseVersionedRequest {
    @OwnershipCheck(EnumOwnershipCheckTables.LIABILITY)
    @NotNull
    private Long id;
    @Min(0)
    private Long amount;
    @Min(0)
    private Long paid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPaid() {
        return paid;
    }

    public void setPaid(Long paid) {
        this.paid = paid;
    }
}
