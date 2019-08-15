package com.tony.billing.request.taginfo;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;

import javax.validation.constraints.NotNull;

/**
 * @author by TonyJiang on 2017/6/28.
 */
public class TagInfoDelRequest extends BaseRequest {
    @OwnershipCheck(EnumOwnershipCheckTables.TAG_INFO)
    @NotNull
    private Long tagId;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
