package com.tony.billing.request.fund;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 基金部分卖出
 * @author jiangwenjie 2020/7/8
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundPreSalePortionRequest extends BaseRequest {
    @NotNull
    @OwnershipCheck(EnumOwnershipCheckTables.FUND_INFO)
    private Long id;
    @NotNull
    private BigDecimal saleFeeRate;
    @NotNull
    private BigDecimal saleAmount;
    @NotEmpty
    private String assessmentDate;
}
