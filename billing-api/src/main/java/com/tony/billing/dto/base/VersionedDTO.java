package com.tony.billing.dto.base;

import lombok.ToString;

/**
 * @author jiangwenjie 2019-03-18
 */
@ToString
public class VersionedDTO extends BaseDTO {
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
