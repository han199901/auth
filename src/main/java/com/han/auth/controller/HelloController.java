package com.han.auth.controller;

import com.han.auth.base.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/api/hello")
    public RestResponse hello(){
        return RestResponse.ok("hello");
    }
}
