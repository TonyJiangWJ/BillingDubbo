package com.tony.billing.constants.enums;

/**
 * @author jiangwj20966 2018/10/26
 */
public enum EnumYesOrNo {
    /**
     *
     */
    YES("Y", 1, "是"),
    NO("N", 0, "否");

    private String code;
    private Integer val;
    private String desc;

    EnumYesOrNo(String code, Integer val, String desc) {
        this.code = code;
        this.val = val;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public Integer val() {
        return val;
    }
}
