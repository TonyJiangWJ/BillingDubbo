package com.tony.billing.dao.mapper;

import com.tony.billing.dao.mapper.base.AbstractPageMapper;
import com.tony.billing.entity.FundPreSaleInfo;
import com.tony.billing.entity.query.FundPreSaleInfoQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangwj20966 2020/07/06
 */
@Repository
public interface FundPreSaleInfoMapper extends AbstractPageMapper<FundPreSaleInfo, FundPreSaleInfoQuery> {

    /**
     * 查询时间段内某一个基金的预卖出日期列表
     *
     * @param dateAfter  开始时间
     * @param dateBefore 结束时间
     * @param fundCode   基金编码
     * @return
     */
    List<String> listPurchaseDatesInRange(@Param("dateAfter") String dateAfter,
                                          @Param("dateBefore") String dateBefore,
                                          @Param("fundCode") String fundCode);
}
