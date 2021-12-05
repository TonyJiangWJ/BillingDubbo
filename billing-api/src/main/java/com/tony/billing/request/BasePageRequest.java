package com.tony.billing.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jiangwenjie 2020/11/11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BasePageRequest extends BaseRequest{
    private Integer pageNo;
    private Integer pageSize;
    private String orderBy;
}
