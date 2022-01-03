package com.xsc.ecommerce.filter;

import com.alibaba.fastjson.JSON;
import com.xsc.ecommerce.constans.CommonConstant;
import com.xsc.ecommerce.constant.GatewayConstant;
import com.xsc.ecommerce.util.TokenParseUtil;
import com.xsc.ecommerce.vo.JwtToken;
import com.xsc.ecommerce.vo.LoginUserInfo;
import com.xsc.ecommerce.vo.UsernameAndPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Jakexsc
 * 2022/1/3
 */
@Component
public class GlobalLoginOrRegisterFilter implements GatewayFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(GlobalLoginOrRegisterFilter.class);

    @Resource
    private RestTemplate restTemplate;
    /**
     * 从注册中心获取客户端信息
     */
    @Resource
    private LoadBalancerClient loadBalancerClient;

    /**
     * 登录，注册，鉴权
     * 1.如果是登录或注册,则去授权中心拿到token并返回给客户端
     * 2.如果是访问其他的服务,则鉴权，没有权限则返回401
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 1.如果是登录
        String path = request.getURI().getPath();
        if (path.contains(GatewayConstant.LOGIN_URI)) {
            String token = getTokenFromAuthorityCenter(request, GatewayConstant.AUTHORITY_CENTER_TOKEN_URI_FORMAT);
            return setResponse(response, token);
        }

        // 2.如果是注册
        if (path.contains(GatewayConstant.REGISTER_URI)) {
            String token = getTokenFromAuthorityCenter(request, GatewayConstant.AUTHORITY_CENTER_REGISTER_URI_FORMAT);
            return setResponse(response, token);
        }

        // 3.访问其他的服务，则鉴权，校验是否能够从token解析出用户的信息
        HttpHeaders headers = request.getHeaders();
        String token = headers.getFirst(CommonConstant.JWT_USER_INFO_KEY);
        LoginUserInfo loginUserInfo;

        try {
            loginUserInfo = TokenParseUtil.parseUserInfoFromToken(token);
            if (loginUserInfo == null) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        } catch (Exception e) {
            LOG.error("parse token error: [{}]", e.getMessage(), e);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> setResponse(ServerHttpResponse response, String token) {
        response.getHeaders().add(CommonConstant.JWT_USER_INFO_KEY, null == token ? "null" : token);
        response.setStatusCode(HttpStatus.OK);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }

    /**
     * 从授权中心获取token
     *
     * @param request
     * @param uriFormat
     * @return String
     */
    private String getTokenFromAuthorityCenter(ServerHttpRequest request, String uriFormat) {
        // 负载均衡获取服务实例信息
        ServiceInstance serviceInstance = loadBalancerClient.choose(CommonConstant.AUTHORITY_CENTER_SERVER_ID);
        LOG.info("Nacos client info: [{}], [{}], [{}]",
                serviceInstance.getServiceId(),
                serviceInstance.getInstanceId(),
                JSON.toJSONString(serviceInstance.getMetadata()));
        String requestUrl = String.format(uriFormat, serviceInstance.getHost(), serviceInstance.getPort());
        UsernameAndPassword requestBody = JSON.parseObject(parseBodyFromRequest(request),
                UsernameAndPassword.class);
        LOG.info("request Url and Body: [{}], [{}]", requestUrl, JSON.toJSONString(requestBody));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JwtToken token = restTemplate.postForObject(requestUrl,
                new HttpEntity<>(JSON.toJSONString(requestBody), headers),
                JwtToken.class);
        if (null != token) {
            return token.getToken();
        }
        return null;
    }

    /**
     * 解析body的数据
     *
     * @param request
     * @return String
     */
    private String parseBodyFromRequest(ServerHttpRequest request) {
        Flux<DataBuffer> body = request.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        // 订阅缓冲区去消费请求体中的数据
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            // 将缓存的body的数据release掉 否则会内存泄漏
            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }
}
