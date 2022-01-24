package com.xsc.ecommerce.filter;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author Jakexsc
 * 2022/1/16
 */
@Component
@WebFilter(filterName = "hystrixRequestContextServlet", urlPatterns = "/**", asyncSupported = true)
public class HystrixRequestContextServlet implements Filter {
    private static final Logger LOG = LoggerFactory.getLogger(HystrixRequestContextServlet.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 初始化Hystrix请求上下文
        // 在不同context缓存不共享
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            this.hystrixConcurrencyStrategyConfig();
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // 关闭Hystrix上下文
            context.shutdown();
        }
    }

    public void hystrixConcurrencyStrategyConfig() {
        try {
            HystrixConcurrencyStrategy target = HystrixConcurrencyStrategyDefault.getInstance();
            HystrixConcurrencyStrategy concurrencyStrategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
            if (concurrencyStrategy instanceof HystrixConcurrencyStrategyDefault) {
                return;
            }

            HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();

            HystrixPlugins.reset();

            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerConcurrencyStrategy(target);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);

            LOG.info("config hystrix concurrency strategy success");
        } catch (Exception e) {
            LOG.error("Failed to register Hystrix Concurrency Strategy: [{}]", e.getMessage(), e);
        }
    }
}
