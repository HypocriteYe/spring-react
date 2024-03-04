package com.hg.webflux.converter;

import com.hg.webflux.pojo.TAuthor;
import com.hg.webflux.pojo.TBook;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 19:20
 */
@ReadingConverter   // 读取数据库数据的时候，把row转成对应对象
public class BookConverter implements Converter<Row, TBook> {
    @Override
    public TBook convert(Row source) {
        if (Objects.isNull(source)) {
            return null;
        }
        return TBook.builder()
                .id(source.get("id", Long.class))
                .title(source.get("title", String.class))
                .authorId(source.get("author_id", Long.class))
                .publishTime(source.get("publish_time", Instant.class))
                .tAuthor(TAuthor.builder().id((source.get("author_id", Long.class))).name(source.get("name", String.class)).build()).build();
    }

    @Override
    public <U> Converter<Row, U> andThen(Converter<? super TBook, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}
