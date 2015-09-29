package com.github.kirivasile.technotrack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 29.09.2015.
 */
public class UserStore {
    private Map<String, User> users;

    public UserStore() {
        users = new HashMap<>();
    }

    public User getUser(String name) throws NullPointerException {
        if (name == null) {
            throw new NullPointerException();
        }
        return users.get(name);
    }

    public void addUser(User user) throws NullPointerException {
        if (user == null) {
            throw new NullPointerException();
        }
        users.put(user.getName(), user);
    }
}
