package com.hg.webflux.controller;

import com.alibaba.fastjson.JSON;
import com.hg.webflux.pojo.MyForm;
import com.hg.webflux.pojo.TAuthor;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Duration;

/**
 * @description
 * @Author ygl
 * @Create 2024/2/29 17:22
 */
@RestController
//    @Controller
public class HelloController {

    @Autowired
    private ConnectionFactory connectionFactory;

    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "key", defaultValue = "value", required = false) String key) {
        return "Hello World!!! key=" + key;
    }

    @RequestMapping("/haha")
    public Mono<String> haha() {
//        return Mono.just("哈哈");
        return Mono.just(0)
                .map(i -> 10 / i)
                .map(i -> "haha" + i);
    }

    @RequestMapping("/hehe")
    public Flux<String> hehe() {
        return Flux.just("呵呵", "呵呵", "呵呵", "呵呵", "呵呵", "呵呵", "呵呵", "呵呵", "呵呵", "呵呵");
    }

    @RequestMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> sse() {
//        return Flux.range(1, 10)
//                .map(i -> i + "ha")
//                .delayElements(Duration.ofMillis(500));
        return Flux.range(1, 10)
                .map(i -> ServerSentEvent.builder("haha" + i)
                        .id(i.toString())
                        .event("haha")
                        .comment("hei-" + 1)
                        .build())
                .delayElements(Duration.ofMillis(500));
    }

    @RequestMapping("/responseEntity")
    public ResponseEntity responseEntity() {
        System.out.println("responseEntity request");
        return ResponseEntity.status(305)
                .header("aaa", "bbb")
//                .contentType(MediaType.APPLICATION_CBOR)
                .body("Hello World");
    }

    @RequestMapping("/rendering")
    public Rendering rendering() {
        // 不能使用@ResponseBodu注解
        return Rendering.redirectTo("/sse").build();
    }

    @RequestMapping("/form")
    public String handleFormUpload(MyForm form, BindingResult errors) {
        System.out.println(JSON.toJSONString(form));
        System.out.println(JSON.toJSONString(errors.getAllErrors()));
        return "success";
    }

    @RequestMapping("/r2dbc")
    public String r2dbc() {

        // 2、获取连接
        Mono.from(connectionFactory.create())
                .flatMapMany(connection -> connection.createStatement("select * from t_author")
//                        .bind("id", 2L)
                        .execute())
                .flatMap(result -> result.map(readable -> {
                    Long id = readable.get("id", Long.class);
                    String name = readable.get("name", String.class);
                    return new TAuthor(id, name);
                }))
                .subscribe(tAuthor -> System.out.println("tAuthor = " + tAuthor));
        return "success";
    }

    @Test
    public void testContext() {
        Context.of("key", "value")
                .putAll(Context.of("key1", "value1"))
                .stream().forEach(System.out::println);
    }

    @Test
    public void testContext2() {
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap(s -> Mono.deferContextual(ctx ->
                        Mono.just(s + " " + ctx.get(key))))
                .contextWrite(ctx -> ctx.put(key, "World"));

        r.subscribe(System.out::println);
    }

    @Test
    public void testContext3() {
        String key = "message";
        Mono<String> r = Mono.just("Hello")
                .flatMap( s -> Mono
                        .deferContextual(ctxView -> Mono.just(s + " " + ctxView.get(key)))
                )
                .flatMap( s -> Mono
                        .deferContextual(ctxView -> Mono.just(s + " " + ctxView.get(key)))
                        .contextWrite(ctx -> ctx.put(key, "Reactor"))
                )
                .contextWrite(ctx -> ctx.put(key, "World"));
        r.subscribe(System.out::println);
    }

    @Test
    public void testSign() {
        char[] c = new char[]{'h', 'e', 'l', 'l', 'o'};
        String s = "hello";
        System.out.println(c.equals(s));
        System.out.println(s.equals(c));
    }
}
