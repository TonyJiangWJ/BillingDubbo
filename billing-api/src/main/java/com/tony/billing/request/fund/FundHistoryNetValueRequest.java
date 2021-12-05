package com.tony.billing.request.fund;

import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author jiangwenjie 2020/9/14
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundHistoryNetValueRequest extends BaseRequest {
    @NotEmpty
    private String fundCode;
    private String targetStartDate;
    private String targetEndDate;
}
