package com.han.auth.services.impl;

import com.han.auth.entity.User;
import com.han.auth.mapper.UserMapper;
import com.han.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Override
    public User getUserByUserName(String username) {
        return userMapper.selectByUserName(username);
    }
}
