package com.hg.webflux;

import com.hg.webflux.pojo.RolesDTO;
import com.hg.webflux.pojo.UserDTO;
import com.hg.webflux.pojo.entity.PermPO;
import com.hg.webflux.pojo.entity.TAuthor;
import com.hg.webflux.pojo.entity.TBook;
import com.hg.webflux.pojo.TBookAuthor;
import com.hg.webflux.repository.AuthorRepositories;
import com.hg.webflux.repository.BookRepositories;
import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    PasswordEncoder passwordEncoder;

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
                    return new TAuthor(id, name, null);
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
                .map(map -> new TAuthor((Long) map.get("id"), (String) map.get("name"), null))
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
                .map(row -> TBookAuthor.builder()
                        .id((Long) row.get("id"))
                        .title(row.get("title").toString())
                        .authorId((Long) row.get("author_id"))
                        .publishTime((Instant) row.get("publish_time"))
                        .tAuthor(TAuthor.builder().id((Long) row.get("author_id")).name(row.get("name").toString()).build()))
                .subscribe(System.out::println);
    }

    @Test
    public void author() throws Exception{
        authorRepositories.findById(1L).subscribe(System.out::println);
        Thread.sleep(3000);
    }

    @Test
    public void authorBookOneToN() throws Exception {
        databaseClient.sql("SELECT a.id author_id, a.`name`, b.id book_id, b.title, b.publish_time\n" +
                "FROM t_author a LEFT JOIN t_book b\n" +
                "ON a.id = b.author_id order by a.id")
                .fetch()
                .all()
                // 比较对象需要重新equals方法
                .bufferUntilChanged(rowMap -> Long.parseLong(rowMap.get("author_id").toString()))
                .map(list -> {
                    TAuthor tAuthor = new TAuthor();
                    tAuthor.setId((Long) list.get(0).get("author_id"));
                    tAuthor.setName(list.get(0).get("name").toString());
                    List<TBook> books = list.stream().map(map -> {
                        TBook tBook = new TBook();
                        tBook.setId((Long) map.get("book_id"));
                        tBook.setAuthorId((Long) map.get("author_id"));
                        tBook.setTitle(map.get("title").toString());
                        String publishTime = map.get("publish_time").toString();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX['['VV']']");

                        Instant instant = OffsetDateTime.parse(publishTime, formatter).atZoneSameInstant(ZoneId.of("Asia/Shanghai")).toInstant();
                        tBook.setPublishTime(instant);
                        return tBook;
                    }).collect(Collectors.toList());
                    tAuthor.setBooks(books);
                    return tAuthor;
                })
                .subscribe(System.out::println);
        Thread.sleep(3000);
    }


    @Test
    public void passwordEncode() {
        System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));
    }
    @Test
    public void sql() throws Exception {
        List<UserDTO> block = databaseClient.sql("SELECT u.id uid, u.username, u.`password`, u.email, u.phone, r.id rid, r.name, r.`value`, p.id pid , p.description, p.uri, p.`value`\n" +
                        "FROM t_user u LEFT JOIN t_user_role ur ON u.id = ur.user_id\n" +
                        "LEFT JOIN t_roles r ON r.id = ur.role_id \n" +
                        "LEFT JOIN t_role_perm rp ON rp.role_id = r.id\n" +
                        "LEFT JOIN t_perm p ON rp.perm_id = p.id\n" +
                        "WHERE u.username = ?")
                .bind(0, "zhangsan")
                .fetch()
                .all()
                .bufferUntilChanged(row -> Long.parseLong(row.get("uid").toString()))
                .publishOn(Schedulers.boundedElastic())
                .map(roleList -> {

                    UserDTO userDTO = new UserDTO();
                    userDTO.setId((Long) roleList.get(0).get("uid"));
                    userDTO.setUsername(roleList.get(0).get("username").toString());
                    userDTO.setPassword(roleList.get(0).get("password").toString());
                    userDTO.setEmail(roleList.get(0).get("email").toString());
                    userDTO.setPhone(roleList.get(0).get("phone").toString());

                    // roleList
                    List<RolesDTO> rolesDTOList = Flux.fromIterable(roleList)
                            .bufferUntilChanged(l -> l.get("rid").toString())
                            .map(permList -> {
                                RolesDTO rolesDTO = new RolesDTO();
                                rolesDTO.setId((Long) permList.get(0).get("rid"));
                                rolesDTO.setName(roleList.get(0).get("name").toString());
                                rolesDTO.setValue(roleList.get(0).get("value").toString());
                                // permList
                                List<PermPO> permPOList = permList.stream().map(perm -> PermPO.builder()
                                        .id(Long.parseLong(perm.get("pid").toString()))
                                        .uri(perm.get("uri").toString())
                                        .description(perm.get("description").toString())
                                        .value(perm.get("value").toString())
                                        .build()).toList();
                                rolesDTO.setPermList(permPOList);
                                return rolesDTO;
                            }).collectList().block();
                    userDTO.setRoleList(rolesDTOList);
                    return userDTO;
                }).collectList().block();
        if (CollectionUtils.isEmpty(block)) return;
        Mono.just(block.get(0)).subscribe(System.out::println);
        Thread.sleep(2000);
    }
}
