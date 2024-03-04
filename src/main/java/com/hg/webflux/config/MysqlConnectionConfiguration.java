package com.hg.webflux.config;

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 16:58
 */
@Configuration
public class MysqlConnectionConfiguration {

    @Autowired
    private R2dbcProperties r2dbcProperties;

//    @Bean
//    public ConnectionFactory connectionFactory(){
//        MySqlConnectionConfiguration configuration = MySqlConnectionConfiguration.builder()
//                .host("localhost")
//                .port(3306)
//                .username(r2dbcProperties.getUsername())
//                .password(r2dbcProperties.getPassword())
//                .database(r2dbcProperties.getName())
//                .serverZoneId(ZoneId.of("Asia/Shanghai"))
//                .build();
//        MySqlConnectionFactory connectionFactory = MySqlConnectionFactory.from(configuration);
//        return connectionFactory;
//    }
}
