package com.xsc.ecommerce.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Jakexsc
 * 2021/12/25
 */
@Service
@Slf4j
public class NacosClientService {
    private final DiscoveryClient discoveryClient;

    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * 打印Nacos Client信息到日志中
     * @param serviceId
     */
    public List<ServiceInstance> getNacosClientInfoById(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }
}
