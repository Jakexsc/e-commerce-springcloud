package com.xsc.ecommerce.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Jakexsc
 * 2021/12/29
 */
@Configuration
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    private static final Logger LOG = LoggerFactory.getLogger(DynamicRouteServiceImpl.class);

    public DynamicRouteServiceImpl(RouteDefinitionWriter routeDefinitionWriter, RouteDefinitionLocator routeDefinitionLocator) {
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.routeDefinitionLocator = routeDefinitionLocator;
    }

    /**
     * 获取路由定义
     */
    private final RouteDefinitionLocator routeDefinitionLocator;

    /**
     * 路由写定义
     */
    private final RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 设置路由实体
     *
     * @param routeDefinition
     * @return String
     */
    public String setRouteDefinition(RouteDefinition routeDefinition) {
        LOG.info("gateway add route: [{}]", routeDefinition);

        routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));

        return "success";
    }

    /**
     * 根据id删除RouteDefinition
     *
     * @param id 标识id
     * @return String
     */
    private String deleteById(String id) {
        try {
            LOG.info("gateway delete router id:[{}]", id);
            this.routeDefinitionWriter.delete(Mono.just(id)).subscribe();
            // 发布事件通知 gateway更新路由定义
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            return "delete success";
        } catch (Exception e) {
            LOG.error("delete gateway router fail, id: [{}]", id);
            return "delete fail";
        }
    }

    /**
     * 更新 = 删除 + 新增
     *
     * @param routeDefinition
     * @return String
     */
    private String updateRouteByDefinition(RouteDefinition routeDefinition) {
        try {
            LOG.info("update gateway route definition");
            this.routeDefinitionWriter.delete(Mono.just(routeDefinition.getId()));
        } catch (Exception exception) {
            LOG.error("update gateway route fail, id: [{}]", routeDefinition);
            return "update fail, not find route id: " + routeDefinition.getId();
        }

        try {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception exception) {
            return "update fail";
        }
    }

    public String updateList(List<RouteDefinition> routeDefinitionList) {
        LOG.info("gateway update route: [{}]", JSON.toJSONString(routeDefinitionList));

        // 先拿到gateway存储的路由定义
        List<RouteDefinition> routeDefinitionExistList = routeDefinitionLocator.getRouteDefinitions()
                .buffer().blockFirst();
        if (CollectionUtils.isNotEmpty(routeDefinitionExistList)) {
            routeDefinitionExistList.forEach(routeDefinition -> {
                LOG.info("delete routeDefinition: [{}]", routeDefinition);
                deleteById(routeDefinition.getId());
            });

        }

        routeDefinitionList.forEach(this::updateRouteByDefinition);
        return "success";

    }
}
