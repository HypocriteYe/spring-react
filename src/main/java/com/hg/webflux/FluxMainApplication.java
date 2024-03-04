package com.hg.webflux;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;
import java.net.URI;

/**
 * @description
 * @Author ygl
 * @Create 2024/2/29 16:13
 */
public class FluxMainApplication {

    public static void main(String[] args) throws IOException {
        // 快速编写一个能处理请求的服务器

        // 1、创建能处理http请求的处理器
        HttpHandler handler = (ServerHttpRequest request, ServerHttpResponse response) -> {
            URI uri = request.getURI();
            // 业务处理
            System.out.println(Thread.currentThread() + "接收到请求：" + uri);
            // 获取响应头
//            System.out.println(response.getHeaders());
            // 获取Cookie
//            System.out.println(response.getCookies());
            // 获取响应状态码
//            System.out.println(response.getStatusCode());
            // buffer工厂
            DataBufferFactory factory = response.bufferFactory();
            DataBuffer dataBuffer = factory.wrap((uri + "Hello !").getBytes());
            // 把xx写出去，需要dataBuffer的发布者
//            response.writeWith(Mono.just(dataBuffer));
            // 响应结束
//            response.setComplete();
            return response.writeWith(Mono.just(dataBuffer));

        };

        // 2、启动一个服务器，监听8080端口，接收数据，拿到数据交给HttpHandler进行请求处理
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);

        // 3、启动netty服务器
        HttpServer.create()
                .host("localhost")
                .port(8080)
                .handle(adapter)
                .bindNow();

        System.out.println("服务器启动...监听8080端口");
        System.in.read();
        System.out.println("服务器停止...");
    }
}
