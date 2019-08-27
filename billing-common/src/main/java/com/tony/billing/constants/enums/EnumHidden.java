package com.tony.billing.constants.enums;

import java.util.HashMap;

/**
 * @author by TonyJiang on 2017/6/10.
 */
public enum EnumHidden {
    /**
     *
     */
    NOT_FILTER(null, "不筛选"),
    HIDDEN(1, "隐藏"),
    NOT_HIDDEN(0, "显示");
    private Integer val;
    private String desc;

    private static HashMap<String, EnumHidden> pool = new HashMap<>();
    private static HashMap<Integer, EnumHidden> poolVal = new HashMap<>();

    static {
        for (EnumHidden enumType : EnumHidden.values()) {
            pool.put(enumType.desc(), enumType);
            poolVal.put(enumType.val(), enumType);
        }
    }

    EnumHidden(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public Integer val() {
        return val;
    }

    public String desc() {
        return desc;
    }

    public static EnumHidden getHiddenEnum(String desc) {
        return pool.get(desc);
    }

    public static EnumHidden getHiddenEnum(Integer val) {
        return poolVal.get(val);
    }
}
