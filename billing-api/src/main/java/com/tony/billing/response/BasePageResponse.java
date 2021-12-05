package com.tony.billing.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author jiangwenjie 2020/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasePageResponse<T> extends BaseResponse {
    private Integer totalPage;
    private Integer pageSize;
    private Integer pageNo;
    private Integer totalItem;
    private List<T> items;
}
