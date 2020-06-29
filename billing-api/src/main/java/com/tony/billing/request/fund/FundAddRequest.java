package com.tony.billing.request.fund;

import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author jiangwenjie 2020/6/29
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundAddRequest extends BaseRequest {
    @NotEmpty
    private String fundName;
    @NotEmpty
    private String fundCode;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseConfirmedDate;
    @NotEmpty
    private String purchaseCost;
    @NotEmpty
    private String purchaseAmount;
    @NotEmpty
    private String purchaseValue;
    @NotEmpty
    private String purchaseFee;
}
