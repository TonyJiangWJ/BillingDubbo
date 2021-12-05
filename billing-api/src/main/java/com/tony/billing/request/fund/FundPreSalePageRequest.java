package com.tony.billing.request.fund;

import com.tony.billing.request.BasePageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jiangwenjie 2020/11/11
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundPreSalePageRequest extends BasePageRequest {
    private String fundCode;
    private String fundName;
}
