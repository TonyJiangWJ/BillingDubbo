package com.tony.billing.request.fund;

import com.tony.billing.constraints.OwnershipCheck;
import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;
import com.tony.billing.request.BaseVersionedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author jiangwenjie 2020/7/6
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundUpdateRequest extends BaseVersionedRequest {
    @NotNull
    @OwnershipCheck(EnumOwnershipCheckTables.FUND_INFO)
    private Long id;
    private String fundName;
    private String fundCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseConfirmDate;
    private BigDecimal purchaseCost;
    private BigDecimal purchaseAmount;
    private BigDecimal purchaseValue;
    private BigDecimal purchaseFee;

}
