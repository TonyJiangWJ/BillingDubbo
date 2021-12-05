package com.tony.billing.entity.query;

import com.tony.billing.entity.FundPreSaleInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jiangwenjie 2020/11/12
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundPreSaleInfoQuery extends BaseQuery<FundPreSaleInfo> {
    private String fundCode;
    private String fundName;
}
