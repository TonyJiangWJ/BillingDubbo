package com.tony.billing.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jiangwenjie 2019-03-18
 */
@Data
@EqualsAndHashCode
@ToString
public class BaseEntity implements Serializable {
    private Long id;
    private Integer isDeleted;
    private Date createTime;
    private Date modifyTime;
}
