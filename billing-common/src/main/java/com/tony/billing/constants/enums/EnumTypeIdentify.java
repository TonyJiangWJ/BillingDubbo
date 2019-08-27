package com.tony.billing.constants.enums;

/**
 * @author TonyJiang 2018/6/21
 */
public enum EnumTypeIdentify {
    /**
     *
     */
    ASSET("A", "资产"),
    LIABILITY("L", "负债");
    private String identify;
    private String desc;

    EnumTypeIdentify(String identify, String desc) {
        this.identify = identify;
        this.desc = desc;
    }

    public String getIdentify() {
        return identify;
    }

    public String getDesc() {
        return desc;
    }
}
