package com.tony.billing.request.budget;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;
import lombok.Data;

/**
 * @author jiangwenjie 2019-03-25
 */
@Data
public class BudgetDeleteRequest extends BaseRequest {
    @OwnershipCheck(value = EnumOwnershipCheckTables.BUDGET)
    private Long id;
}
