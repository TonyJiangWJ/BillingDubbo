package com.tony.billing.request.fund;

import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author jiangwenjie 2020/7/1
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundInfoQueryRequest extends BaseRequest {
    @NotEmpty
    private String fundCode;

    private String purchaseDate;
}
