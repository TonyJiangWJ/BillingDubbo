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
public class CostRecordQuery extends BaseQuery<CostRecord> {
    private String startDate;
    private String endDate;
    private String content;
    private Integer isDeleted;
    private String tradeNo;
    private String orderNo;
    private String costCreateTime;
    private String paidTime;
    private String costModifyTime;
    private String location;
    private String orderType;
    private String target;
    private String goodsName;
    private Long money;
    private String inOutType;
    private String orderStatus;
    private Long serviceCost;
    private Long refundMoney;
    private String memo;
    private String tradeStatus;
    private Integer isHidden;
    private Long userId;
    private Long id;
}
