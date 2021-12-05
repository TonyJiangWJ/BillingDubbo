package com.tony.billing.service.impl;

import com.tony.billing.service.BaseServiceNoTransactionalTest;
import com.tony.billing.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jiangwenjie 2021/2/24
 */
@Slf4j
public class RedisTest extends BaseServiceNoTransactionalTest {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void test01() {
        redisUtils.set("test_key", "123456");
        log.info("key value: {}", redisUtils.get("test_key", String.class).get());
    }
}
