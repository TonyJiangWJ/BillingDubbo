package com.tony.billing.listeners;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * @author jiangwenjie 2020/9/9
 */
public class TestExecutionListener extends AbstractTestExecutionListener {

    private Logger logger = LoggerFactory.getLogger(TestExecutionListener.class);

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
//        通过META-INF/spring.factories注册 LoggingListener 即可实现和直接运行时一样的效果
//        readYaml();
    }

    private void readYaml() {
        Yaml yaml = new Yaml();
        Map<Object, Object> map = yaml.load(this.getClass().getClassLoader().getResourceAsStream("application.yml"));
        if (map.containsKey("log4j2")) {
            String path = ((Map<String, String>)map.get("log4j2")).get("path");
            if (StringUtils.isNotEmpty(path)) {
                System.setProperty("log4j2.path", path);
            }
        }
    }

    @Override
    public int getOrder() {
        return LoggingApplicationListener.DEFAULT_ORDER - 1;
    }
}
