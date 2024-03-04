package com.hg.webflux;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 14:50
 */
public class MonoTest {


    @Test
    public void test() {
        Mono<List<Integer>> mono = Mono.just(Arrays.asList(1, 2, 3, 4, 5));

        // 使用 flatMap 将每个元素映射为一个 Mono 流，并将这些 Mono 流合并为一个新的 Mono 流
        Mono<Flux<Integer>> result = Mono.just(mono.flatMapMany(Flux::fromIterable));


        // 使用 flatMapMany 将每个元素映射为一个 Flux 流，并将这些 Flux 流合并为一个新的 Flux 流
        Flux<Integer> result2 = mono.flatMapMany(Flux::fromIterable);

    }
}
