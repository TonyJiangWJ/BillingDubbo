package com.tony.billing.service.api;


import com.tony.billing.constraints.enums.EnumOwnershipCheckTables;

/**
 * @author jiangwenjie 2019-03-20
 */
public interface CommonValidateService {
    /**
     * 校验用户和数据的所有权
     * @param table 表枚举数据
     * @param primaryId 主键值
     * @return 校验通过返回true
     */
    boolean validateOwnership(EnumOwnershipCheckTables table, Object primaryId);
}
