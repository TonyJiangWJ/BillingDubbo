package com.tony.billing.service.api.base;

import com.tony.billing.entity.base.BaseEntity;
import com.tony.billing.entity.query.BaseQuery;

/**
 * @author jiangwenjie 2020/11/12
 */
public interface AbstractPageService<T extends BaseEntity, Q extends BaseQuery<T>> extends AbstractService<T> {
    /**
     * 分页查询
     *
     * @param query
     * @return
     */
    Q page(Q query);
}
