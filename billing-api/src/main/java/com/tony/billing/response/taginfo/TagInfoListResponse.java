package com.tony.billing.response.taginfo;

import com.tony.billing.dto.TagInfoDTO;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author by TonyJiang on 2017/6/15.
 */
public class TagInfoListResponse extends BaseResponse {
    private List<TagInfoDTO> tagInfoList;

    public List<TagInfoDTO> getTagInfoList() {
        return tagInfoList;
    }

    public void setTagInfoList(List<TagInfoDTO> tagInfoList) {
        this.tagInfoList = tagInfoList;
    }
}
