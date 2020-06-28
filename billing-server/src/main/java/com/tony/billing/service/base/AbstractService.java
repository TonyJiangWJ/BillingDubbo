package com.tony.billing.service.base;

import com.google.common.base.Preconditions;
import com.tony.billing.constants.enums.EnumDeleted;
import com.tony.billing.dao.mapper.base.AbstractMapper;
import com.tony.billing.entity.base.BaseEntity;
import com.tony.billing.entity.base.BaseVersionedEntity;
import com.tony.billing.util.UserIdContainer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author jiangwenjie 2019-03-18
 */
public abstract class AbstractService<T extends BaseEntity, M extends AbstractMapper<T>> {

    /**
     * 获取注入的mapper对象
     *
     * @return mapper对象
     */
    @Autowired
    protected M mapper;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新增对象到数据库
     *
     * @param entity 存储对象
     * @return 插入成功返回插入后的id，失败返回-1
     */
    public Long insert(T entity) {
        entity.setModifyTime(new Date());
        entity.setCreateTime(new Date());
        entity.setIsDeleted(EnumDeleted.NOT_DELETED.val());
        if (entity instanceof BaseVersionedEntity) {
            ((BaseVersionedEntity)entity).setVersion(0);
        }
        if (mapper.insert(entity) > 0) {
            return entity.getId();
        } else {
            return -1L;
        }
    }

    /**
     * 更新，自动判断是否需要version乐观锁
     *
     * @param entity 更新对象
     * @return 更新成功返回true
     */
    public boolean update(T entity) {
        Preconditions.checkNotNull(entity.getId(), "id must not be null");
        if (entity instanceof BaseVersionedEntity) {
            Preconditions.checkNotNull(((BaseVersionedEntity)entity).getVersion(), "version must not be null");
        }
        entity.setModifyTime(new Date());
        return mapper.update(entity) > 0;
    }


    public T getById(Long id) {
        return mapper.getById(id, UserIdContainer.getUserId());
    }

    public List<T> list(T condition) {
        List<T> result = mapper.list(condition);
        if (CollectionUtils.isNotEmpty(result)) {
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public boolean deleteById(Long id) {
        return mapper.deleteById(id, UserIdContainer.getUserId()) > 0;
    }
}
