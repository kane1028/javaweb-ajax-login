package com.pzy.model;

/**
 * 用户实体类（只有用户名和密码两个字段，够用就好）
 */
public class User {

    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
