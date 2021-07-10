package com.han.auth.services;

import com.han.auth.entity.User;

public interface AuthenticationService {
    boolean authUser(User user, String username, String password);

    String pwdEncode(String password);
}