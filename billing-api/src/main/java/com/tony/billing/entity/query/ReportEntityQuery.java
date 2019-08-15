package com.tony.billing.entity.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author by TonyJiang on 2017/6/11.
 */
@Data
@ToString
@EqualsAndHashCode
public class ReportEntityQuery {
    private String datePrefix;
    private Long userId;
}
