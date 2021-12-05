package com.tony.billing.dao.mapper.base;

import com.tony.billing.entity.base.BaseEntity;
import com.tony.billing.entity.query.BaseQuery;

import java.util.List;

/**
 * @author jiangwenjie 2020/11/12
 */
public interface AbstractPageMapper<T extends BaseEntity, Q extends BaseQuery<T>> extends AbstractMapper<T> {
    /**
     * 查询总数量
     *
     * @param query 分页查询参数
     * @return 返回符合条件的总数
     */
    Integer count(Q query);

    /**
     * 分页查询，查询当页数据列表
     *
     * @param query 分页查询参数
     * @return 返回符合条件的当页数据列表
     */
    List<T> page(Q query);
}
