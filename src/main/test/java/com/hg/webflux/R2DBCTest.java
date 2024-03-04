package com.hg.webflux;

import com.alibaba.fastjson.JSON;
import com.hg.webflux.pojo.TAuthor;
import com.hg.webflux.pojo.TBook;
import com.hg.webflux.repository.AuthorRepositories;
import com.hg.webflux.repository.BookRepositories;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.r2dbc.spi.*;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 14:26
 */
@SpringBootTest
public class R2DBCTest {

    @Autowired
    BookRepositories bookRepositories;

    @Autowired
    AuthorRepositories authorRepositories;

    @Autowired
    R2dbcEntityTemplate r2dbcEntityTemplate;    // CRUD API

    @Autowired
    DatabaseClient databaseClient;  //  数据库客户端，贴近底层，做复杂查询

    // 有了R2DBC，应用在数据库层面天然支持高并发，高吞吐量
    @Test
    public void connection() throws IOException {
        // 1、获取连接工厂

        MySqlConnectionConfiguration configuration = MySqlConnectionConfiguration.builder()
                .host("localhost")
                .port(3306)
                .username("root")
                .password("root")
                .database("school_manage")
                .build();
        MySqlConnectionFactory connectionFactory = MySqlConnectionFactory.from(configuration);
//        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:mysql://101.43.18.192:3310/mycat");


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

    }

    @Test
    public void r2dbcEntityTemplate() throws IOException {
        // 1、构造Criteria查询条件
        Criteria criteria = Criteria.empty()
                .and("id").is(1L)
                .and("name").is("张三");

        // 2、封装Query对象
        Query query = Query.query(criteria);
        r2dbcEntityTemplate.select(query, TAuthor.class)
                .subscribe(System.out::println);
        System.in.read();
    }

    @Test
    public void databaseClient() throws IOException {

        databaseClient.sql("select * from t_author where id = ?;")
                .bind(0, 2L)
                .fetch()
                .all()
                .map(map -> new TAuthor((Long) map.get("id"), (String) map.get("name")))
                .subscribe(System.out::println);
        System.in.read();
    }

    @Test
    public void findAll() throws IOException {
        authorRepositories.findAll().subscribe(System.out::println);
        System.in.read();
    }

    @Test
    public void findAllByIdInAndNameLike() throws IOException {
        authorRepositories.findAllByIdInAndNameLike(List.of(1L, 2L), "张%").subscribe(System.out::println);
        System.in.read();
    }

    @Test
    public void findBookAndAuthor() throws IOException {
        bookRepositories.findBookAndAuthor(1L).subscribe(System.out::println);
        System.in.read();
    }

    @Test
    public void findBookAndAuthorByDataClient() throws Exception {
        databaseClient.sql("select b.*, t.name from t_book b left join t_author t on b.author_id = t.id where b.id = ?")
                .bind(0, 1L)
                .fetch()
                .all()
                .map(row -> TBook.builder()
                        .id((Long) row.get("id"))
                        .title(row.get("title").toString())
                        .authorId((Long) row.get("author_id"))
                        .publishTime((Instant) row.get("publish_time"))
                        .tAuthor(TAuthor.builder().id((Long) row.get("author_id")).name(row.get("name").toString()).build()))
                .subscribe(System.out::println);
    }
}
