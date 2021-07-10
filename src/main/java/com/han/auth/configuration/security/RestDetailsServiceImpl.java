package com.han.auth.configuration.security;

import com.han.auth.services.UserRoleService;
import com.han.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class RestDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserRoleService userRoleService;

    @Autowired
    public RestDetailsServiceImpl(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        com.han.auth.entity.User user = userService.getUserByUserName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username  not found.");
        }

        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        userRoleService.getUserRole(user).forEach(item -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(item.getName()));
        });
        return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
