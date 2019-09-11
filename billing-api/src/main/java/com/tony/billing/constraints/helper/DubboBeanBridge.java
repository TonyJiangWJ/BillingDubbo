package com.tony.billing.constraints.helper;

import com.tony.billing.service.api.CommonValidateService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 * @author jiangwenjie 2019/9/10
 */
@Component
public class DubboBeanBridge {
    @Reference
    private CommonValidateService commonValidateService;

    public CommonValidateService getCommonValidateService() {
        return commonValidateService;
    }
}
