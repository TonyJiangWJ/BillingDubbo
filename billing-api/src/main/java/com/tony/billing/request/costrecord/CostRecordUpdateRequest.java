package com.tony.billing.request.costrecord;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseVersionedRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author by TonyJiang on 2017/6/9.
 */
public class CostRecordUpdateRequest extends BaseVersionedRequest {
    @OwnershipCheck(EnumOwnershipCheckTables.COST_RECORD)
    @NotEmpty
    private String tradeNo;
    private String goodsName;
    private String memo;
    private String location;
    private String orderType;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
