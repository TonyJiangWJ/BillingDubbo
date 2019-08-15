package com.tony.billing.constraints.enums;

/**
 * @author jiangwenjie 2019-03-20
 */
public enum EnumOwnershipCheckTables {
    /**
     *
     */
    ASSET("t_asset", "资产表"),
    LIABILITY("t_liability", "负债表"),
    ASSET_TYPES("t_asset_types", "资产负债类型表"),
    BUDGET("t_budget", "预算表"),
    COST_RECORD("t_cost_info", "tradeNo","userId","账单表"),
    COST_RECORD_WITH_ID("t_cost_info", "id","userId","账单表"),
    TAG_INFO("t_tag_info", "标签信息表"),
    ;
    private String tableName;
    private String primaryKey;
    private String userIdentifyKey;
    private String tableDesc;

    EnumOwnershipCheckTables(String tableName, String primaryKey, String userIdentifyKey, String tableDesc) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.userIdentifyKey = userIdentifyKey;
        this.tableDesc = tableDesc;
    }

    EnumOwnershipCheckTables(String tableName, String tableDesc) {
        this.tableName = tableName;
        this.tableDesc = tableDesc;
    }

    EnumOwnershipCheckTables(String tableName, String userIdentifyKey, String tableDesc) {
        this.tableName = tableName;
        this.userIdentifyKey = userIdentifyKey;
        this.tableDesc = tableDesc;
    }


    public String getTableName() {
        return tableName;
    }

    public String getPrimaryKey() {
        return primaryKey == null ? "id" : primaryKey;
    }

    public String getUserIdentifyKey() {
        return userIdentifyKey == null ? "userId" : userIdentifyKey;
    }

    public String getTableDesc() {
        return tableDesc;
    }

}
