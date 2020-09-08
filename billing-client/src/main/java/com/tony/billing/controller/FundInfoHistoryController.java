package com.tony.billing.controller;

import com.tony.billing.service.api.FundInfoHistoryService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangwenjie 2020/9/8
 */
@RestController
@RequestMapping("/bootDemo")
public class FundInfoHistoryController extends BaseController {
    @Reference
    private FundInfoHistoryService fundInfoHistoryService;

}
