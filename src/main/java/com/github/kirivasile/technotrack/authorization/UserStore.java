package com.github.kirivasile.technotrack.authorization;

import java.util.List;

/**
 * Created by Kirill on 09.11.2015.
 */
public interface UserStore {
    User getUser(int id) throws Exception;
    List<User> getUserByName(String name) throws Exception;
    int addUser(User user) throws Exception;
    List<User> getUserList() throws Exception;
    void close() throws Exception;
}
