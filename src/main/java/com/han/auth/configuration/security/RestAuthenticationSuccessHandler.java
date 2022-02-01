package com.han.auth.configuration.security;

import com.han.auth.base.SystemCode;
import com.han.auth.services.UserService;
import com.han.auth.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;

    @Autowired
    public RestAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User springUser = (User) authentication.getPrincipal();
        com.han.auth.entity.User user = userService.getUserByUserName(springUser.getUsername());
        com.han.auth.entity.User newUser = new com.han.auth.entity.User();
        newUser.setUsername(user.getUsername());
        List<String> roleList = new ArrayList<>();
        authentication.getAuthorities().forEach(item -> {
            roleList.add(item.getAuthority());
        });
        Map<String, Object> map = new HashMap<>();
//        map.put("user", newUser);
        map.put(JwtTokenUtils.TOKEN_HEADER, JwtTokenUtils.TOKEN_PREFIX + JwtTokenUtils.createToken(newUser.getUsername(), roleList));
        RestUtil.response(response, SystemCode.OK.getCode(), SystemCode.OK.getMessage(), map);
    }
}
