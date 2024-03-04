package com.hg.webflux.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 13:16
 */
@Component
public class MyWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        System.out.println("请求处理放行到目标方法之前...");
        Mono<Void> filter = chain.filter(exchange)
                .doOnError(err -> System.out.println("目标方法执行出错..."))
                .doFinally(signalType -> System.out.println("目标方法执行之后..."));
        return filter;
    }
}
