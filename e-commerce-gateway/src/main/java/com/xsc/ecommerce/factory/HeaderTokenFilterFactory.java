package com.xsc.ecommerce.factory;

import com.xsc.ecommerce.filter.HeaderTokenFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @author Jakexsc
 * 2021/12/30
 */
@Component
public class HeaderTokenFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new HeaderTokenFilter();
    }
}
