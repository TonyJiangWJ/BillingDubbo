package com.tony.billing.service;

import com.tony.billing.service.api.DubboDemoService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author jiangwenjie 2019-07-22
 */
@Service
public class DubboDemoServiceImpl implements DubboDemoService {

    @Override
    public String sayHello(String name) {
        return "Hello, " + name;
    }
}
