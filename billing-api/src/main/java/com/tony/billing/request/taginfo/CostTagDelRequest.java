package com.tony.billing.request.taginfo;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author by TonyJiang on 2017/6/25.
 */
public class CostTagDelRequest extends BaseRequest {

    @OwnershipCheck(EnumOwnershipCheckTables.COST_RECORD)
    @NotEmpty
    private String tradeNo;
    @OwnershipCheck(EnumOwnershipCheckTables.TAG_INFO)
    @NotNull
    private Long tagId;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
