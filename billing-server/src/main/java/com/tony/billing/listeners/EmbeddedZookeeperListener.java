package com.tony.billing.listeners;

import com.tony.billing.bootstrap.EmbeddedZooKeeper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

/**
 * @author jiangwenjie 2020/9/9
 */
public class EmbeddedZookeeperListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private Logger logger = LoggerFactory.getLogger(EmbeddedZookeeperListener.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        Environment environment = event.getEnvironment();
        boolean useEmbeddedZookeeper = Boolean.parseBoolean(environment.getProperty("embedded.zookeeper.enable"));
        logger.debug("embedded zookeeper enable: {}", useEmbeddedZookeeper);
        if (useEmbeddedZookeeper) {
            String zookeeperPort = environment.getProperty("embedded.zookeeper.port");
            if (StringUtils.isEmpty(zookeeperPort)) {
                zookeeperPort = "2181";
            }
            int port = Integer.parseInt(zookeeperPort);
            new EmbeddedZooKeeper(port, false).start();
        }
    }
}
