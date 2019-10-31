/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tony.billing.bootstrap;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Dubbo Registry ZooKeeper Provider Bootstrap
 *
 * @author jiangwenjie
 * @since 2.7.0
 */
@EnableAutoConfiguration
@MapperScan("com.tony.billing.dao.mapper")
@ComponentScan("com.tony.billing")
public class DubboRegistryZooKeeperProviderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DubboRegistryZooKeeperProviderApplication.class)
                .listeners(
                        (ApplicationListener<ApplicationEnvironmentPreparedEvent>) event -> {
                            Environment environment = event.getEnvironment();
                            boolean useEmbeddedZookeeper = Boolean.parseBoolean(environment.getProperty("embedded.zookeeper.enable"));
                            if (useEmbeddedZookeeper) {
                                String zookeeperPort = environment.getProperty("embedded.zookeeper.port");
                                if (StringUtils.isEmpty(zookeeperPort)) {
                                    zookeeperPort = "2181";
                                }
                                int port = Integer.parseInt(zookeeperPort);
                                new EmbeddedZooKeeper(port, false).start();
                            }
                        }
                ).run(args);
    }
}
