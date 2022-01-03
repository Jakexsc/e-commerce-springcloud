package com.xsc.ecommerce.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Jakexsc
 * 2022/1/3
 */
@Configuration
public class GatewayBeanConf {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
