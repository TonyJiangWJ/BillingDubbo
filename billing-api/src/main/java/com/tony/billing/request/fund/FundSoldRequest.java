package com.tony.billing.request.fund;

import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jiangwenjie 2020/7/6
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundSoldRequest extends BaseRequest {
    @NotEmpty
    private String fundCode;
    @NotEmpty
    private String fundName;
    @NotNull
    private BigDecimal soldValue;
    @NotNull
    private BigDecimal soldAmount;
    @NotNull
    private BigDecimal purchaseCost;
    @NotNull
    private BigDecimal purchaseFee;
    @NotNull
    private BigDecimal soldIncome;
    @NotNull
    private BigDecimal soldFee;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date soldDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date soldConfirmDate;
}
