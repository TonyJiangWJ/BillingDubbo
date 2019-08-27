package com.tony.billing.constants.enums;

/**
 * <p>
 * </p>
 * <li></li>
 *
 * @author jiangwj20966 2018/2/22
 */
public enum EnumLiabilityStatus {
    /**
     *
     */
    UNPAID(0, "未还清"),
    PAID(1, "已还清");

    EnumLiabilityStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    private Integer status;
    private String desc;

    public Integer getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
