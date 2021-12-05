package com.tony.billing.service.base;

import com.tony.billing.dao.mapper.base.AbstractPageMapper;
import com.tony.billing.entity.base.BaseEntity;
import com.tony.billing.entity.query.BaseQuery;
import com.tony.billing.service.api.base.AbstractPageService;
import com.tony.billing.util.UserIdContainer;

/**
 * @author jiangwenjie 2020/11/12
 */
public abstract class AbstractPageServiceImpl<T extends BaseEntity, Q extends BaseQuery<T>, M extends AbstractPageMapper<T, Q>>
        extends AbstractServiceImpl<T, M> implements AbstractPageService<T, Q> {
    @Override
    public Q page(Q query) {
        if (query.getUserId() == null) {
            query.setUserId(UserIdContainer.getUserId());
        }
        Integer count = this.mapper.count(query);
        query.setTotalItem(count);
        if (count > 0) {
            query.setItems(this.mapper.page(query));
        }
        return query;
    }
}
