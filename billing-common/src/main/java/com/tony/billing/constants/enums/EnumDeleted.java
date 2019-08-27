package com.tony.billing.constants.enums;

import java.util.HashMap;

/**
 * @author by TonyJiang on 2017/6/11.
 */
public enum EnumDeleted {
    /**
     *
     */
    NOT_FILTER(null, "不筛选"),
    DELETED(1, "已删除"),
    NOT_DELETED(0, "未删除");
    private Integer val;
    private String desc;

    private static HashMap<String, EnumDeleted> pool = new HashMap<>();
    private static HashMap<Integer, EnumDeleted> poolVal = new HashMap<>();

    static {
        for (EnumDeleted enumType : EnumDeleted.values()) {
            pool.put(enumType.desc(), enumType);
            poolVal.put(enumType.val(), enumType);
        }
    }

    EnumDeleted(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public Integer val() {
        return val;
    }

    public String desc() {
        return desc;
    }

    public static EnumDeleted getDeletedEnum(String desc) {
        return pool.get(desc);
    }

    public static EnumDeleted getDeletedEnum(Integer val) {
        return poolVal.get(val);
    }
}
