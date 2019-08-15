package com.tony.billing.service;

import com.tony.billing.service.api.DubboDemoService;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author jiangwenjie 2019-08-14
 */
@Service(version = "1.0.x")
public class DubboDemoServiceImpl2 implements DubboDemoService {
    @Override
    public String sayHello(String name) {
        return "Go fxxk yourself," + name;
    }
}
