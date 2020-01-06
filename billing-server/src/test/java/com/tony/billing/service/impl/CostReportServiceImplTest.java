package com.tony.billing.service.impl;


import com.alibaba.fastjson.JSON;
import com.tony.billing.entity.ReportEntity;
import com.tony.billing.service.BaseServiceTest;
import com.tony.billing.service.api.CostReportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiangwenjie 2020/1/6
 */
@Slf4j
public class CostReportServiceImplTest extends BaseServiceTest {

    @Resource
    private CostReportService costReportService;

    @Test
    public void getReportByDatePrefix() {
        long start = System.currentTimeMillis();
        List<ReportEntity> reports = costReportService.getReportByDatePrefix(new ArrayList<String>() {{
            add("2019-07");
            add("2019-08");
            add("2019-09");
            add("2019-10");
            add("2019-11");
            add("2019-12");
        }}, 2L);
        long cost = System.currentTimeMillis() - start;
        log.info("get report cost: {}ms infos: {}", cost, JSON.toJSONString(reports));

    }
}
