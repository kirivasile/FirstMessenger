package com.github.kirivasile.technotrack.authorization;

import java.util.List;

/**
 * Created by Kirill on 09.11.2015.
 */
public interface UserStore {
    User getUser(int id);
    User getUserByName(String name);
    int addUser(User user);
    List<User> getUserList();
    void close() throws Exception;
}
