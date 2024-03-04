package com.hg.webflux.converter;

import com.hg.webflux.pojo.entity.TAuthor;
import com.hg.webflux.pojo.TBookAuthor;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.Instant;
import java.util.Objects;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 19:20
 * 自定义converter后，单表的简单查询也会使用converter，因此如果没有连表查询的字段会报错
 * 解决方案一：连表查询使用VO对象 + converter，简单查询使用PO
 * 解决方案二：在自定义converter中对元数据进行判断，是否包含连表的字段，决定如何封装
 */
@ReadingConverter   // 读取数据库数据的时候，把row转成对应对象
public class BookAuthorConverter implements Converter<Row, TBookAuthor> {

    @Override
    public TBookAuthor convert(Row source) {
        if (Objects.isNull(source)) {
            return null;
        }
        return TBookAuthor.builder()
                .id(source.get("id", Long.class))
                .title(source.get("title", String.class))
                .authorId(source.get("author_id", Long.class))
                .publishTime(source.get("publish_time", Instant.class))
                .tAuthor(TAuthor.builder().id((source.get("author_id", Long.class))).name(source.get("name", String.class)).build()).build();
    }

    @Override
    public <U> Converter<Row, U> andThen(Converter<? super TBookAuthor, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
