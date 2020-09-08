package com.tony.billing.response.fund;

import com.tony.billing.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * @author jiangwenjie 2020/9/3
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundHistoryNetValueResponse extends BaseResponse {
    private String fundCode;
    private String fundName;
    private List<Map<String, String>> historyNetValues;
}
