package com.github.kirivasile.technotrack.authorization;

import java.io.Serializable;

/**
 * Created by Kirill on 29.09.2015.
 */
public class User {
    private String name;
    private String password;
    private String nickname;

    public User() {}

    public User(String name, String password, String nickname) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getNick() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
