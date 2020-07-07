package com.tony.billing.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jiangwenjie 2020/7/7
 */
@Data
@ToString
@EqualsAndHashCode
public class FundExistenceCheck implements Serializable {
    @NotEmpty
    private String fundCode;
    @NotEmpty
    private String confirmDate;
    @NotNull
    private BigDecimal purchaseAmount;
}
