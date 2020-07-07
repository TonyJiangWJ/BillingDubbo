package com.tony.billing.response.fund;

import com.tony.billing.model.FundExistenceCheck;
import com.tony.billing.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author jiangwenjie 2020/7/7
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundsExistenceCheckResponse extends BaseResponse {
    private List<FundExistenceCheck> existsList;
}
