package com.xsc.ecommerce.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;

import java.util.Collections;
import java.util.List;

/**
 * @author Jakexsc
 * 2022/1/16
 */
public class CacheHystrixCommand extends HystrixCommand<List<ServiceInstance>> {
    private static final Logger LOG = LoggerFactory.getLogger(CacheHystrixCommand.class);

    private final NacosClientService nacosClientService;
    private final String serviceId;

    private static final HystrixCommandKey CACHE_KEY = HystrixCommandKey.Factory.asKey("CacheHystrixCommand");

    public CacheHystrixCommand(NacosClientService nacosClientService, String serviceId) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CacheHystrixCommandGroup"))
                .andCommandKey(CACHE_KEY));
        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    @Override
    protected List<ServiceInstance> run() throws Exception {
        return this.nacosClientService.getNacosClientInfoById(serviceId);
    }

    @Override
    protected List<ServiceInstance> getFallback() {
        return Collections.emptyList();
    }

    @Override
    protected String getCacheKey() {
        return serviceId;
    }

    public static void flushCache(String serviceId) {
        HystrixRequestCache.getInstance(CACHE_KEY, HystrixConcurrencyStrategyDefault.getInstance())
                .clear(serviceId);
        LOG.info("flush request cache in hystrix command: [{}], [{}]", serviceId,
                Thread.currentThread().getName());
    }
}
