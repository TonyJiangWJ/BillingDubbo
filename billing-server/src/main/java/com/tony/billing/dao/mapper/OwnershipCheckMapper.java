package com.tony.billing.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author jiangwenjie 2019-03-20
 */
@Repository
public interface OwnershipCheckMapper {

    /**
     * 校验所有权是否是当前用户的，是则返回大于零
     *
     * @param tableName       表名称
     * @param primaryKey      主键名称
     * @param primaryValue    主键值
     * @param userIdentifyKey 用户userId字段名称
     * @param userId          用户userId值
     * @return 拥有所有权则返回大于零
     */
    @Select("SELECT COUNT(1) FROM ${tableName} WHERE ${primaryKey}=#{primaryValue} AND ${userIdentifyKey}=#{userId}")
    int countByPrimaryKeyAndUserId(@Param("tableName") String tableName,
                                   @Param("primaryKey") String primaryKey,
                                   @Param("primaryValue") Object primaryValue,
                                   @Param("userIdentifyKey") String userIdentifyKey,
                                   @Param("userId") Long userId);
}
