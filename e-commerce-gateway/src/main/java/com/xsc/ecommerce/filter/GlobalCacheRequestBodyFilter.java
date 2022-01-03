package com.xsc.ecommerce.filter;

import com.xsc.ecommerce.constant.GatewayConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 将登录、注册的请求内容进行包装
 *
 * @author Jakexsc
 * 2022/1/3
 */
@Component
public class GlobalCacheRequestBodyFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalCacheRequestBodyFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        boolean isLoginOrRegister = exchange.getRequest().getURI().getPath().contains(GatewayConstant.LOGIN_URI)
                || exchange.getRequest().getURI().getPath().contains(GatewayConstant.REGISTER_URI);

        if (exchange.getRequest().getHeaders().getContentType() == null || !isLoginOrRegister) {
            return chain.filter(exchange);
        }

        // 拿到Http请求的数据 --> dataBuffer
        return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
            // 确保缓冲区的数据不被释放
            DataBufferUtils.retain(dataBuffer);
            Flux<DataBuffer> cacheFlux = Flux.defer(() ->
                Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
            // 重新包装 serverHttpRequest，重写getBody方法，能够返回数据
            ServerHttpRequestDecorator serverHttpRequestDecorator = new ServerHttpRequestDecorator(exchange.getRequest()) {
                @Override
                public Flux<DataBuffer> getBody() {
                    return cacheFlux;
                }
            };
            // 将包装之后的serverHttpRequest继续向下传递
            return chain.filter(exchange.mutate().request(serverHttpRequestDecorator).build());
        });
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
