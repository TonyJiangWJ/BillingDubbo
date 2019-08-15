package com.tony.billing.request.costrecord;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author by TonyJiang on 2017/6/2.
 */
public class CostRecordDetailRequest extends BaseRequest {
    @OwnershipCheck(EnumOwnershipCheckTables.COST_RECORD)
    @NotEmpty
    private String tradeNo;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
