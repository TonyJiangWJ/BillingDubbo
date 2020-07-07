package com.tony.billing.request.fund;

import com.tony.billing.model.FundAddModel;
import com.tony.billing.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author jiangwenjie 2020/7/7
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundBatchAddRequest extends BaseRequest {
    @NotEmpty
    private List<FundAddModel> fundInfoList;
}
