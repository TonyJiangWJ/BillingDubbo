package com.tony.billing.response.costrecord;

import com.tony.billing.dto.CostRecordDetailDTO;
import com.tony.billing.response.BaseResponse;

/**
 * @author by TonyJiang on 2017/6/2.
 */
public class CostRecordDetailResponse extends BaseResponse {
    private CostRecordDetailDTO recordDetail;

    public CostRecordDetailDTO getRecordDetail() {
        return recordDetail;
    }

    public void setRecordDetail(CostRecordDetailDTO recordDetail) {
        this.recordDetail = recordDetail;
    }
}
