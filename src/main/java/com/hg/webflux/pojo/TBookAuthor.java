package com.hg.webflux.pojo;

import com.hg.webflux.pojo.entity.TAuthor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 19:10
 */
@Table("t_book")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TBookAuthor {

    @Id
    private Long id;
    private String title;
    private Long authorId;
    private Instant publishTime;

    private TAuthor tAuthor;
}
