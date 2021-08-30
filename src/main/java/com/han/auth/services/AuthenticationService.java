package com.han.auth.services;

import com.han.auth.entity.User;

public interface AuthenticationService {
    boolean authUser(User user, String username, String password);

    User registerUserWithPhone(String phone, int role);

    String pwdEncode(String password);
}