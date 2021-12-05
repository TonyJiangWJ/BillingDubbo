package com.tony.billing.response.fund;

import com.tony.billing.entity.query.FundPreSaleInfoQuery;
import com.tony.billing.model.FundPreSaleModel;
import com.tony.billing.response.BasePageResponse;
import com.tony.billing.util.ResponseUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

/**
 * @author jiangwenjie 2020/11/11
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FundPreSalePageResponse extends BasePageResponse<FundPreSaleModel> {
    public FundPreSalePageResponse() {
    }

    public FundPreSalePageResponse(FundPreSaleInfoQuery query) {
        ResponseUtil.success(this);
        this.setPageNo(query.getPageNo());
        this.setPageSize(query.getPageSize());
        this.setTotalItem(query.getTotalItem());
        this.setTotalPage(query.getTotalPage());
        if (CollectionUtils.isNotEmpty(query.getItems())) {
            this.setItems(query.getItems().stream().map(preSale -> {
                FundPreSaleModel fundPreSaleModel = new FundPreSaleModel();
                BeanUtils.copyProperties(preSale, fundPreSaleModel);
                return fundPreSaleModel;
            }).collect(Collectors.toList()));
        }
    }
}
