package com.hg.webflux.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

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
}
