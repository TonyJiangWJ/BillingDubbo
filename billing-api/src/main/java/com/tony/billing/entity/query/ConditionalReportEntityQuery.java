package com.tony.billing.entity.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author jiangwenjie 2020/1/6
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ConditionalReportEntityQuery extends ReportEntityQuery {
    private String inOutType;
    private Integer isDeleted;
    private Integer isHidden;
}
