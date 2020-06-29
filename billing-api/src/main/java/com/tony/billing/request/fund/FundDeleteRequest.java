package com.tony.billing.request.fund;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author jiangwenjie 2020/6/30
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundDeleteRequest extends BaseRequest {

    @NotNull
    @OwnershipCheck(value = EnumOwnershipCheckTables.FUND_INFO)
    private Long id;
}
