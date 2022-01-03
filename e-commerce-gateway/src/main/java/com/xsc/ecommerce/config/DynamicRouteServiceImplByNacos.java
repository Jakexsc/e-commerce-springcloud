package com.xsc.ecommerce.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * 通过nacos下发动态路由配置,监听nacos中路由配置变更
 *
 * @author Jakexsc
 * 2021/12/29
 */
@Component
@DependsOn({"gatewayConfig"})
public class DynamicRouteServiceImplByNacos {
    private static final Logger LOG = LoggerFactory.getLogger(DynamicRouteServiceImplByNacos.class);

    private ConfigService configService;

    @Resource
    private DynamicRouteServiceImpl dynamicRouteServiceImpl;

    /**
     * 初始化
     * 1.读取到nacos的路由配置
     * 2.初始化configService
     * 3.初始化Route
     */
    @PostConstruct
    public void init() {
        LOG.info("gateway route init...");
        try {
            ConfigService configService = initConfigService();
            if (configService == null) {
                LOG.error("init config service fail");
                return;
            }

            // 通过 Nacos config 并指定路由配置去获取路由配置
            String configInfo = configService.getConfig(
                    GatewayConfig.NACOS_ROUTER_DATA_ID,
                    GatewayConfig.NACOS_GROUP_ID,
                    GatewayConfig.DEFAULT_TIMEOUT
            );
            LOG.info("get current gateway config: [{}]", configInfo);
            List<RouteDefinition> routeDefinitionList = JSON.parseArray(configInfo, RouteDefinition.class);
            routeDefinitionList.forEach(routeDefinition -> {
                LOG.info("init gateway config: [{}]", routeDefinition.toString());
                dynamicRouteServiceImpl.setRouteDefinition(routeDefinition);
            });

            dynamicRouteByNacosListener(GatewayConfig.NACOS_ROUTER_DATA_ID, GatewayConfig.NACOS_GROUP_ID);
        } catch (Exception e) {
            LOG.error("gateway route init has some error: [{}]", e.getMessage(), e);
        }
    }

    private ConfigService initConfigService() {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", GatewayConfig.NACOS_SERVER_ADDRESS);
        properties.setProperty("namespace", GatewayConfig.NACOS_NAMESPACE);
        try {
            return configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            LOG.error("init gateway nacos config error: [{}]", e.getMessage(), e);
            return null;
        }
    }

    private void dynamicRouteByNacosListener(String dataId, String group) {
        try {
            configService.addListener(dataId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                /**
                 * Nacos最新的配置定义
                 * @param configInfo Nacos最新的定义
                 */
                @Override
                public void receiveConfigInfo(String configInfo) {
                    LOG.info("start update config: [{}]", configInfo);
                    List<RouteDefinition> routeDefinitionList = JSON.parseArray(configInfo, RouteDefinition.class);
                    LOG.info("update route: [{}]", routeDefinitionList.toString());
                    dynamicRouteServiceImpl.updateList(routeDefinitionList);
                }
            });
        } catch (NacosException e) {
            LOG.error("dynamic update gateway config error: [{}]", e.getMessage(), e);
        }
    }
}
