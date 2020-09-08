package com.tony.billing.constants.enums;

/**
 * @author jiangwenjie 2020/9/3
 */
public enum EnumFundChangeType {
    /**
     *
     */
    SALE_PORTION(1, "部分售出"),
    ENHANCE(2, "增强");

    private Integer type;
    private String desc;

    EnumFundChangeType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
