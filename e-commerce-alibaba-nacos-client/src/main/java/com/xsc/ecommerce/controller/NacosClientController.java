package com.xsc.ecommerce.controller;

import com.xsc.ecommerce.service.NacosClientService;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Jakexsc
 * 2021/12/25
 */
@RestController
@RequestMapping("/nacos-client")
public class NacosClientController {
    @Resource
    private NacosClientService nacosClientService;

    @GetMapping("/service-instance")
    public List<ServiceInstance> logNacosClientInfo(
            @RequestParam(defaultValue = "e-commerce-alibaba-nacos-client") String serviceId) {
        return nacosClientService.getNacosClientInfoById(serviceId);
    }
}
