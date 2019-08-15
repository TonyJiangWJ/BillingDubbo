package com.tony.billing.dto;

import com.tony.billing.dto.base.BaseDTO;

/**
 * @author jiangwenjie 2019-03-24
 */
public class BudgetOverviewDTO extends BaseDTO {
    private String yearMonth;
    private String used;
    private String remain;
    private String totalAmount;

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public String getUsed() {
        return used;
    }

    public void setUsed(String used) {
        this.used = used;
    }

    public String getRemain() {
        return remain;
    }

    public void setRemain(String remain) {
        this.remain = remain;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
