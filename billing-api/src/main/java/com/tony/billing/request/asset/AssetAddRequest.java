package com.tony.billing.request.asset;

import com.tony.billing.request.BaseRequest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author TonyJiang on 2018/7/12
 */
public class AssetAddRequest extends BaseRequest {
    @NotNull
    private Long type;
    private String name;
    @NotNull
    @Min(0)
    private Long amount;
    private Boolean available;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
