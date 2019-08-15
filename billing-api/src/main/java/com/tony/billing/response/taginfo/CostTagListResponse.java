package com.tony.billing.response.taginfo;

import com.tony.billing.dto.TagInfoDTO;
import com.tony.billing.response.BaseResponse;

import java.util.List;

/**
 * @author by TonyJiang on 2017/6/15.
 */
public class CostTagListResponse extends BaseResponse {
    private List<TagInfoDTO> tagInfoModels;

    public List<TagInfoDTO> getTagInfoModels() {
        return tagInfoModels;
    }

    public void setTagInfoModels(List<TagInfoDTO> tagInfoDTOS) {
        this.tagInfoModels = tagInfoDTOS;
    }
}
