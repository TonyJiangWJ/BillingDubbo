package com.tony.billing.service.api.base;

import com.tony.billing.entity.base.BaseEntity;

import java.util.List;

/**
 * @author jiangwenjie 2020/7/6
 */
public interface AbstractService<T extends BaseEntity> {
    /**
     * 新增对象到数据库
     *
     * @param entity 存储对象
     * @return 插入成功返回插入后的id，失败返回-1
     */
    Long insert(T entity);

    /**
     * 更新，自动判断是否需要version乐观锁
     *
     * @param entity 更新对象
     * @return 更新成功返回true
     */
    boolean update(T entity);

    /**
     * 根据id获取详情
     *
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 根据id删除记录
     *
     * @param id
     * @return
     */
    boolean deleteById(Long id);

    /**
     * 根据条件获取列表
     *
     * @param condition
     * @return
     */
    List<T> list(T condition);
}
