package com.tony.billing.entity.query;

import com.tony.billing.entity.CostRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author by TonyJiang on 2017/6/9.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CostRecordQuery extends CostRecord {
    private String startDate;
    private String endDate;
    private String content;

}
