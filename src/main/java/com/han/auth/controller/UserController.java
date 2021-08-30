package com.han.auth.controller;


import com.han.auth.base.RestResponse;
import com.han.auth.base.SystemCode;
import com.han.auth.configuration.tool.api.JiGuangApi;
import com.han.auth.configuration.tool.entity.LoginTokenVerifyBean;
import com.han.auth.configuration.tool.entity.PhoneAuthenticationBean;
import com.han.auth.entity.Role;
import com.han.auth.entity.User;
import com.han.auth.services.AuthenticationService;
import com.han.auth.services.UserRoleService;
import com.han.auth.utils.JsonUtil;
import com.han.auth.utils.JwtTokenUtils;
import com.han.auth.utils.RSADecrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    @Value("${jiguang.auth}")
    private String auth;
    @Value("${jiguang.rsa.private.key}")
    private String key;

    private final RestTemplate restTemplate;
    private final AuthenticationService authenticationService;
    private final UserRoleService userRoleService;

    @Autowired
    public UserController(RestTemplate restTemplate, AuthenticationService authenticationService, UserRoleService userRoleService) {
        this.restTemplate = restTemplate;
        this.authenticationService = authenticationService;
        this.userRoleService = userRoleService;
    }

    @PostMapping("/register/phone")
    public RestResponse registerPhone(@RequestBody LoginTokenVerifyBean authenticationBean){
        if(null == authenticationBean || null == authenticationBean.getLoginToken()) {
            return RestResponse.fail(SystemCode.ParameterValidError.getCode(),"参数错误");
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
            headers.setBasicAuth(auth);
            HttpEntity<LoginTokenVerifyBean> httpEntity = new HttpEntity<>(authenticationBean,headers);
            PhoneAuthenticationBean result = restTemplate.postForObject(JiGuangApi.loginTokenVerify, httpEntity, PhoneAuthenticationBean.class);
            if(result.getCode() == SystemCode.JiguangVerifySuccess.getCode()) {
                String phone = RSADecrypt.decrypt(result.getPhone(),key);
                User user = authenticationService.registerUserWithPhone(phone,2);
                List<String> roleList = userRoleService.getUserRoleName(user);
                String token = JwtTokenUtils.createToken(user.getUsername(),roleList);
                return RestResponse.ok(token);
            }
        } catch (Exception e) {
            return RestResponse.fail(500,"服务器爆炸了");
        }
        return RestResponse.fail(500,"服务器爆炸了");
    }

}
