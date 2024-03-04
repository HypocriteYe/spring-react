package com.hg.webflux.config;

import com.hg.webflux.converter.BookConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 17:59
 */
@EnableR2dbcRepositories    // 开启R2DBC仓库功能 jpa
@Configuration
public class R2DBCConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public R2dbcCustomConversions conversions() {
        // 加入自定义的转换器
        return R2dbcCustomConversions.of(MySqlDialect.INSTANCE, new BookConverter());
    }
}
