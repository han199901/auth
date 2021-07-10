package com.han.auth.configuration.property;

public class CookieConfig {

    public static String getName() {
        return "han_app";
    }

    public static Integer getInterval() {
        return 30 * 24 * 60 * 60;
    }
}
