package com.hg.webflux.repository;

import com.hg.webflux.pojo.entity.TAuthor;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 18:01
 */
@Repository
public interface AuthorRepositories extends R2dbcRepository<TAuthor, Long> {

    // 默认基础了一堆CRUD方法，向mybatis-plus

    // QBC: Query By Criteria
    // QBE: Query By Example

    Flux<TAuthor> findAllByIdInAndNameLike(Collection<Long>id, String name);

    // 自定义Query注解，指定sql语句
    @Override
    @Query("select * from  t_author")
    Flux<TAuthor> findAll();

    // 1-1 关联
    // 1-N 关联
    // 一本书有唯一的作者，一个作者有多本书籍


}
