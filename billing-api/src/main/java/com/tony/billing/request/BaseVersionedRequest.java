package com.tony.billing.request;

import javax.validation.constraints.NotNull;

/**
 * @author jiangwenjie 2019-03-20
 */
public class BaseVersionedRequest extends BaseRequest {
    @NotNull
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
