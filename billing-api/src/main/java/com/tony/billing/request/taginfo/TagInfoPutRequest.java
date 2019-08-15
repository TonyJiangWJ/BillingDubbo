package com.tony.billing.request.taginfo;

import com.tony.billing.request.BaseRequest;
import javax.validation.constraints.NotEmpty;

/**
 * @author by TonyJiang on 2017/6/25.
 */
public class TagInfoPutRequest extends BaseRequest {
    @NotEmpty
    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
