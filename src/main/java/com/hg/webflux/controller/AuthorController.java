package com.hg.webflux.controller;

import com.hg.webflux.pojo.entity.TAuthor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @description
 * @Author ygl
 * @Create 2024/3/1 17:07
 */
@RestController
public class AuthorController {

    @GetMapping("/author")
    public Flux<TAuthor> getAuthor() {
        return Flux.empty();
    }
}
