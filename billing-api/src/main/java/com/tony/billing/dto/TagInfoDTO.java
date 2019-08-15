package com.tony.billing.dto;

import com.tony.billing.dto.base.BaseDTO;

/**
 * @author by TonyJiang on 2017/6/15.
 */
public class TagInfoDTO extends BaseDTO {
    private String tagName;
    private Long tagId;
    private Long usageCount;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public Long getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Long usageCount) {
        this.usageCount = usageCount;
    }
}
