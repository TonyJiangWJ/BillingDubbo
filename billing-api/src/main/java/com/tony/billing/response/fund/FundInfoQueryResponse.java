package com.tony.billing.response.fund;

import com.tony.billing.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jiangwenjie 2020/7/1
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundInfoQueryResponse extends BaseResponse {
    private String fundCode;
    private String fundName;
    private String fundConfirmedValue;
    private String fundConfirmedDate;
}
