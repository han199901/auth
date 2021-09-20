package com.han.auth.controller;

import com.han.auth.base.RestResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @PostMapping("/api/hello")
    public RestResponse hello(){
        return RestResponse.ok("hello");
    }

    @GetMapping("/version")
    public RestResponse version() {
        Map<String,Object> version = new HashMap<>();
        version.put("code","0.0.1");
        version.put("force",false);
        return RestResponse.ok(version);
    }
}
