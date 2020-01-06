package com.tony.billing.request.budget;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;
import lombok.Data;

/**
 * @author jiangwenjie 2020/1/6
 */
@Data
public class BudgetDetailRequest extends BaseRequest {
    @OwnershipCheck(value = EnumOwnershipCheckTables.BUDGET)
    private Long id;
}
