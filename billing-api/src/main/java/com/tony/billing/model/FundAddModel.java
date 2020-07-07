package com.tony.billing.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author jiangwenjie 2020/7/7
 */
@Data
@ToString
@EqualsAndHashCode
public class FundAddModel implements Serializable {
    @NotEmpty
    private String fundName;
    @NotEmpty
    private String fundCode;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date purchaseConfirmDate;
    @NotEmpty
    private String purchaseCost;
    @NotEmpty
    private String purchaseAmount;
    @NotEmpty
    private String purchaseValue;
    @NotEmpty
    private String purchaseFee;
}
