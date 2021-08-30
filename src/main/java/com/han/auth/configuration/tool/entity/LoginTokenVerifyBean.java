package com.han.auth.configuration.tool.entity;

import java.io.Serializable;

public class LoginTokenVerifyBean implements Serializable {
    private String loginToken;
    private int exID;

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public int getExID() {
        return exID;
    }

    public void setExID(int exID) {
        this.exID = exID;
    }

    @Override
    public String toString() {
        return "LoginTokenVerifyBean{" +
                "loginToken='" + loginToken + '\'' +
                ", exID=" + exID +
                '}';
    }
}
