package com.han.auth.configuration.security;


import com.han.auth.services.AuthenticationService;
import com.han.auth.services.UserRoleService;
import com.han.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public RestAuthenticationProvider(AuthenticationService authenticationService, UserService userService, UserRoleService userRoleService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        com.han.auth.entity.User user = userService.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        boolean result = authenticationService.authUser(user, username, password);
        if (false == user.getIsAccountNonLocked()) {
            throw new LockedException("用户被禁用");
        }
        else if(false == user.getIsAccountNonExpired()) {
            throw  new AccountExpiredException("账户过期");
        }
        else if (false == user.getIsCredentialsNonExpired()) {
            throw new CredentialsExpiredException("凭证已过期");
        }
        else if (!result) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        userRoleService.getUserRole(user).forEach(item -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(item.getName()));
        });

        User authUser = new User(user.getUsername(), user.getPassword(), grantedAuthorities);
        return new UsernamePasswordAuthenticationToken(authUser, authUser.getPassword(), authUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
