package com.tony.billing.constants.enums;

/**
 * @author jiangwj20966 2018/10/26
 */
public enum EnumYesOrNo {
    /**
     *
     */
    YES("Y", "是"),
    NO("N", "否");

    private String code;
    private String desc;

    EnumYesOrNo(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
