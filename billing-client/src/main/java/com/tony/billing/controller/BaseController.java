package com.tony.billing.controller;

import com.tony.billing.request.BaseRequest;
import com.tony.billing.request.BaseVersionedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by TonyJiang on 2017/6/25.
 */
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean commonValidate(BaseRequest request) {
        if (request instanceof BaseVersionedRequest) {
            return ((BaseVersionedRequest) request).getVersion() != null;
        }
        return true;
    }
}
