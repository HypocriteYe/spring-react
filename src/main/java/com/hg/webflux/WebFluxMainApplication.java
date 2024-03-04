package com.hg.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * @description
 * @Author ygl
 * @Create 2024/2/29 17:20
 */
//@EnableWebFlux // WebFlux完全自定义 WebFluxAutoConfiguration不生效
@SpringBootApplication
public class WebFluxMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebFluxMainApplication.class, args);
    }
}
