package com.han.auth.services;

import com.han.auth.entity.User;

public interface UserService {

    User getUserByUserName(String username);
}
