package com.tony.billing.request.liability;

import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 * @author jiangwj20966 2018/3/5
 */
public class LiabilityAddRequest extends BaseRequest {

    @NotNull
    private Long type;
    @NotNull
    private Date repaymentDay;
    @NotNull
    @Min(0)
    private Long amount;
    @NotNull
    private Integer installment;

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Date getRepaymentDay() {
        return repaymentDay;
    }

    public void setRepaymentDay(Date repaymentDay) {
        this.repaymentDay = repaymentDay;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getInstallment() {
        return installment;
    }

    public void setInstallment(Integer installment) {
        this.installment = installment;
    }
}
