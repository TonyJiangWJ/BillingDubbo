package com.tony.billing.request.costrecord;

import com.tony.billing.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/17
 */
@Data
public class CostRecordBatchHideRequest extends BaseRequest {
    @NotEmpty
    private List<Long> costIds;
    @NotNull
    private Integer isHidden;
}
