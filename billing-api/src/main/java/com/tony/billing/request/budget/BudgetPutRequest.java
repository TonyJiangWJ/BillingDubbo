package com.tony.billing.request.budget;

import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author jiangwj20966 on 2017/7/13.
 */
public class BudgetPutRequest extends BaseRequest {

    @NotNull
    private Long amount;
    @NotEmpty
    private String year;
    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;
    @NotEmpty
    private String name;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
