package com.hodolog.api.controller;

import com.hodolog.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;


@Slf4j
@RestController
public class PostController {

    @PostMapping("/posts")
    public Map<String, String> post(@Valid @RequestBody PostCreate params) {
        log.info("params={}", params);
        return Map.of();
    }
}
