package com.github.kirivasile.technotrack;

import java.io.Serializable;

/**
 * Created by Kirill on 29.09.2015.
 */
public class User implements Serializable {
    private String name;
    private String password;

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    String getPassword() {
        return password;
    }
}
