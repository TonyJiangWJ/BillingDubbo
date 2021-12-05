package com.tony.billing.entity.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangwenjie 2020/11/11
 */
@Data
@EqualsAndHashCode
public class BaseQuery<T> implements Serializable {
    private Long userId;

    private List<T> items;
    private Integer pageNo;
    private Integer totalItem;
    private Integer pageSize;
    private String sort;
    private String orderBy;

    public Integer getPageSize() {
        if (pageSize == null || pageSize == 0) {
            return 20;
        }
        return pageSize;
    }

    public Integer getTotalPage() {
        if (totalItem == null || totalItem == 0) {
            return 0;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 20;
        }
        return totalItem % pageSize == 0 ? (totalItem / pageSize) : (totalItem / pageSize + 1);
    }

    public Integer getIndex() {
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        return (pageNo - 1) * getPageSize();
    }

}
