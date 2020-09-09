package com.tony.billing.service;

import com.tony.billing.TestApplication;
import com.tony.billing.listeners.TestExecutionListener;
import com.tony.billing.util.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangwenjie 2020/1/6
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
@TestExecutionListeners(TestExecutionListener.class)
@Slf4j
public class BaseServiceNoTransactionalTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private RSAUtil rsaUtil;

    @Test
    public void testRsa() {
        log.info("encrypt:{}", rsaUtil.encryptWithPubKey("hello, world"));
    }

}
