package com.tony.billing.response.budget;

import com.tony.billing.dto.BudgetDTO;
import com.tony.billing.response.BaseResponse;
import lombok.Data;

/**
 * @author jiangwenjie 2020/1/6
 */
@Data
public class BudgetDetailResponse extends BaseResponse {
    private BudgetDTO budgetInfo;
}
