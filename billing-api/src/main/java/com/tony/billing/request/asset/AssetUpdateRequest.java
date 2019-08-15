package com.tony.billing.request.asset;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseVersionedRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author jiangwj20966 2018/2/22
 */
public class AssetUpdateRequest extends BaseVersionedRequest {

    @NotNull
    @OwnershipCheck(EnumOwnershipCheckTables.ASSET)
    private Long id;
    @Min(0)
    private Long amount;
    private String name;
    private String available;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
