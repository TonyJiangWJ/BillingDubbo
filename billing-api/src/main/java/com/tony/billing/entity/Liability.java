package com.tony.billing.entity;

import com.tony.billing.entity.base.BaseVersionedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 分期负债
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Liability extends BaseVersionedEntity implements Comparable<Liability> {
    private Long userId;
    private Date repaymentDay;
    private String name;
    private Long type;
    private Long amount;
    private Integer status;
    private Integer installment;
    private Integer index;
    private Long paid;

    @Override
    public int compareTo(Liability o) {
        if (o == null) {
            return 1;
        }
        if (this.repaymentDay.getTime() > o.getRepaymentDay().getTime()) {
            return 1;
        } else if (this.repaymentDay.getTime() == o.getRepaymentDay().getTime()) {
            return 0;
        }
        return -1;
    }
}
