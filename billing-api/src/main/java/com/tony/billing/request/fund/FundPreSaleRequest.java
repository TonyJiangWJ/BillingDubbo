package com.tony.billing.request.fund;

import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author jiangwenjie 2020/6/30
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundPreSaleRequest extends BaseRequest {
    @NotNull
    private List<Long> fundIds;
    @NotNull
    private BigDecimal fundSoldFeeRate;
    @NotEmpty
    private String assessmentDate;

}
