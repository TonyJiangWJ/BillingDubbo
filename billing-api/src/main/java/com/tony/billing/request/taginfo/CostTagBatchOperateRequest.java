package com.tony.billing.request.taginfo;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jiangwenjie 2019-03-28
 */
public class CostTagBatchOperateRequest extends BaseRequest {
    @OwnershipCheck(value = EnumOwnershipCheckTables.TAG_INFO)
    @NotNull
    private Long tagId;
    @NotEmpty
    private List<Long> costIds;

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public List<Long> getCostIds() {
        return costIds;
    }

    public void setCostIds(List<Long> costIds) {
        this.costIds = costIds;
    }
}
