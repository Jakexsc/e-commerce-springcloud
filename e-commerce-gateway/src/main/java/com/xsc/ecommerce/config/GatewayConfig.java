package com.xsc.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jakexsc
 * 2021/12/29
 */
@Configuration
public class GatewayConfig {
    public static final long DEFAULT_TIMEOUT = 30000;

    /**
     * nacos服务地址
     */
    public static String NACOS_SERVER_ADDRESS;

    /**
     * nacos命名空间
     */
    public static String NACOS_NAMESPACE;

    /**
     * nacos dataId
     */
    public static String NACOS_ROUTER_DATA_ID;

    /**
     * nacos groupId
     */
    public static String NACOS_GROUP_ID;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public void setNacosServerAddress(String nacosServerAddress) {
        NACOS_SERVER_ADDRESS = nacosServerAddress;
    }

    @Value("${spring.cloud.nacos.discovery.namespace}")
    public void setNacosNamespace(String nacosNamespace) {
        NACOS_NAMESPACE = nacosNamespace;
    }

    @Value("${nacos.gateway.route.config.data-id}")
    public void setNacosRouterDataId(String nacosRouterDataId) {
        NACOS_ROUTER_DATA_ID = nacosRouterDataId;
    }

    @Value("${nacos.gateway.route.config.group}")
    public void setNacosGroupId(String nacosGroupId) {
        NACOS_GROUP_ID = nacosGroupId;
    }
}
