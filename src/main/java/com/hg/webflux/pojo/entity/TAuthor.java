package com.hg.webflux.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 15:31
 */
@Table("t_author")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TAuthor {

    @Id
    private Long id;
    private String name;

    @Transient
    private List<TBook> books;
}
