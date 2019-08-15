package com.tony.billing.request.taginfo;

import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

import java.util.List;

/**
 * @author jiangwenjie 2019-03-28
 */
public class CommunalTagsRequest extends BaseRequest {
    @NotEmpty
    private List<Long> costIds;

    public List<Long> getCostIds() {
        return costIds;
    }

    public void setCostIds(List<Long> costIds) {
        this.costIds = costIds;
    }
}
