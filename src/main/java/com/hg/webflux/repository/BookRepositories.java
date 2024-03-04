package com.hg.webflux.repository;

import com.hg.webflux.pojo.TBook;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 19:12
 */
@Repository
public interface BookRepositories extends R2dbcRepository<TBook, Long> {

    // 1-1
    @Query("select b.*, t.name from t_book b " +
            "left join t_author t on b.author_id = t.id " +
            "where b.id = ?")
    Mono<TBook> findBookAndAuthor(Long bookId);
}
