package com.nepxion.discovery.guide.gateway.filter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;

import com.nepxion.discovery.guide.gateway.feign.GatewayFeign;

public class MyGatewayFilter implements GlobalFilter, Ordered {
    private static final Logger LOG = LoggerFactory.getLogger(MyGatewayFilter.class);

    @Autowired
    private GatewayFeign gatewayFeign;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public int getOrder() {
        return 10000;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            String parameter = "MyGatewayFilter";
            String feignValue = gatewayFeign.invoke(parameter);
            String restTemplateValue = restTemplate.getForEntity("http://discovery-guide-service-a/rest/" + parameter, String.class).getBody();

            LOG.info("网关上触发Feigin调用，返回值={}", feignValue);
            LOG.info("网关上触发RestTemplate调用，返回值={}", restTemplateValue);
        } catch (Exception e) {
            LOG.info("Invoke failed", e);
        }

        return chain.filter(exchange);
    }
}