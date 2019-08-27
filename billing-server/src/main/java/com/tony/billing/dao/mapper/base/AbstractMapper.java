package com.tony.billing.dao.mapper.base;

import com.tony.billing.entity.base.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jiangwenjie 2019-03-18
 */
public interface AbstractMapper<T extends BaseEntity> {

    /**
     * 通用插入操作
     *
     * @param entity 插入实体对象
     * @return 插入成功的记录数量 大于0则插入成功
     */
    Integer insert(T entity);

    /**
     * 通用更新操作
     *
     * @param entity 更新实体对象
     * @return 更新成功的记录数量 大于0则更新成功
     */
    Integer update(T entity);

    /**
     * 根据id获取数据对象
     *
     * @param id     id
     * @param userId 用户id
     * @return 数据对象
     */
    T getById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 根据id删除记录，set isDeleted=1
     *
     * @param id
     * @param userId
     * @return
     */
    Integer deleteById(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * 根据传入条件查询列表
     *
     * @param condition
     * @return
     */
    List<T> list(T condition);
}
