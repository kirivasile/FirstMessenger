package com.github.kirivasile.technotrack.authorization;

/**
 * Created by Kirill on 09.11.2015.
 */
public interface UserStore {
    User getUser(String name);
    void addUser(User user);
}
