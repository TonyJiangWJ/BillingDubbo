package com.tony.billing.service;

import com.tony.billing.TestApplication;
import com.tony.billing.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangwenjie 2020/1/6
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@Slf4j
public class BaseServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private RSAUtil rsaUtil;

    @Test
    public void testRsa() {
        log.info("encrypt:{}", rsaUtil.encryptWithPubKey("hello, world"));
    }

}
