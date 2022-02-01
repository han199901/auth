package com.han.auth.services.impl;

import com.han.auth.entity.Role;
import com.han.auth.entity.User;
import com.han.auth.services.AuthenticationService;
import com.han.auth.services.UserRoleService;
import com.han.auth.services.UserService;
import com.han.auth.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final UserRoleService roleService;

    @Autowired
    public AuthenticationServiceImpl(UserService userService, UserRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public boolean authUser(User user, String username, String password) {
        if(null == user)
            return false;
        else return password.equals(user.getPassword());
    }

    @Override
    public User registerUserWithPhone(String phone, int role) {
        List<Role> roleList = roleService.getAllRole();
        for (Role value : roleList) {
            if (value.getId() == role) {
                User user = new User();
                user.setUsername(phone);
                user.setIsAccountNonExpired(true);
                user.setIsAccountNonLocked(true);
                user.setIsCredentialsNonExpired(true);
                user.setIsEnable(true);
                user.setPassword(JwtTokenUtils.generatePassayPassword());
                if (userService.save(user) == 1) {
                    Role t = new Role();
                    t.setId(role);
                    roleService.setUserRole(user, t);
                    return user;
                } else {
                    return null;
                }

            }
        }
        return null;
    }


    @Override
    public String pwdEncode(String password) {
        return password;
    }
}