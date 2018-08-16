package com.jia16.marketing.config;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class RocketMQConfiguration {

    private final static Logger logger = Logger.getLogger(RocketMQConfiguration.class);

    @Bean
    @ConfigurationProperties(prefix = "ons.client")
    // 配置选项参考com.aliyun.openservices.ons.api.PropertyKeyConst
    public Properties onsProperties() {
        return new Properties();
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Producer producer(@Qualifier("onsProperties") Properties onsProperties) {
        onsProperties.put(PropertyKeyConst.ProducerId, onsProperties.getProperty("producerId"));
        logger.info("Starting producer, properties: " + onsProperties.toString());
        return ONSFactory.createProducer(onsProperties);
    }

}
