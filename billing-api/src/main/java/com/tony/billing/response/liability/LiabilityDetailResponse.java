package com.tony.billing.response.liability;

import com.tony.billing.dto.LiabilityDTO;
import com.tony.billing.response.BaseResponse;

/**
 * <p>
 * </p>
 * <li></li>
 *
 * @author jiangwj20966 2018/2/22
 */
public class LiabilityDetailResponse extends BaseResponse {
    private LiabilityDTO liability;

    public LiabilityDTO getLiability() {
        return liability;
    }

    public void setLiability(LiabilityDTO liability) {
        this.liability = liability;
    }
}
