package com.hg.webflux.config;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/4 14:23
 */

import com.hg.webflux.component.MyReactiveUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Autowired
    MyReactiveUserDetailService myReactiveUserDetailService;

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                // 1.1、定义哪些请求需要认证，哪些不需要
                .authorizeExchange(authorize -> {

                    // 1.2、允许访问静态资源
                    authorize.matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();

                    // 1.3、剩下的所有请求需要认证（登录）
                    authorize.anyExchange().authenticated();
                });

        // 2、 开启默认表单登录
        http.formLogin();

        // 3、安全控制
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        // 4、配置认证管理器
        http.authenticationManager(new UserDetailsRepositoryReactiveAuthenticationManager(myReactiveUserDetailService));


        return http.build();
    }

    @Primary
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
}
