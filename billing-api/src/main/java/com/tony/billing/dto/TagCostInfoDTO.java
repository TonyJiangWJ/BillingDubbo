package com.tony.billing.dto;

/**
 * @author jiangwenjie 2019-03-25
 */
public class TagCostInfoDTO extends TagInfoDTO {

    public TagCostInfoDTO() {
        amount = 0L;
    }

    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
