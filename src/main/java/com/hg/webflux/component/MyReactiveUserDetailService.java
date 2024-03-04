package com.hg.webflux.component;

import com.hg.webflux.pojo.RolesDTO;
import com.hg.webflux.pojo.UserDTO;
import com.hg.webflux.pojo.entity.PermPO;
import com.hg.webflux.pojo.entity.RolesPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/4 15:11
 */
@Component
public class MyReactiveUserDetailService implements ReactiveUserDetailsService {


    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 自定义如何按照用户名查询用户信息
    @Override
    public Mono<UserDetails> findByUsername(String username) {

        // 从数据库查询用户、角色、权限
        Mono<UserDetails> mono = databaseClient.sql("SELECT u.id uid, u.username, u.`password`, u.email, u.phone, r.id rid, r.name, r.`value`, p.id pid , p.description, p.uri, p.`value`\n" +
                        "FROM t_user u LEFT JOIN t_user_role ur ON u.id = ur.user_id\n" +
                        "LEFT JOIN t_roles r ON r.id = ur.role_id \n" +
                        "LEFT JOIN t_role_perm rp ON rp.role_id = r.id\n" +
                        "LEFT JOIN t_perm p ON rp.perm_id = p.id\n" +
                        "WHERE u.username = ? limit 1")
                .bind(0, username)
                .fetch()
                .one()
                .map(map -> User.builder()
                        .username(username)
                        .password(map.get("password").toString())
                        .passwordEncoder(str -> passwordEncoder.encode(str))
                        // 权限
                        .authorities("download", "view", "delete")
                        .roles("admin", "sale")
                        .build());
        return mono;
    }
}
