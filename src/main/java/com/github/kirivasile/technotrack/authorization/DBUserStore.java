package com.github.kirivasile.technotrack.authorization;

import com.github.kirivasile.technotrack.jdbc.QueryExecutor;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 29.09.2015.
 */
public class DBUserStore implements UserStore {
    private Connection connection;
    private QueryExecutor executor;

    public DBUserStore(Connection conn) {
        this.connection = conn;
        this.executor = new QueryExecutor();
    }

    @Override
    public synchronized List<User> getUserByName(String name) throws Exception {
        if (name == null) {
            return null;
        }
        Map<Integer, Object> queryArgs = new HashMap<>();
        queryArgs.put(1, name);
        return executor.execQuery(connection, "SELECT * FROM USERS where LOGIN = ?", queryArgs, (r) -> {
            List<User> data = new ArrayList<User>();
            while (r.next()) {
                User u = new User(r.getString("login"), r.getString("password"), r.getString("nick"));
                int id = r.getInt("id");
                u.setId(id);
                data.add(u);
            }
            return data;
        });
    }

    @Override
    public synchronized User getUser(int id) throws Exception {
        if (id < 0) {
            return null;
        }
        //return users.get(id);
        Map<Integer, Object> queryArgs = new HashMap<>();
        queryArgs.put(1, id);
        return executor.execQuery(connection, "SELECT * FROM USERS where ID = ?", queryArgs, (r) -> {
            User result = new User();
            while (r.next()) {
                result = new User(r.getString("login"), r.getString("password"), r.getString("nick"));
                result.setId(id);
            }
            return result;
        });
    }

    @Override
    public synchronized int addUser(User user) throws Exception {
        if (user == null) {
            System.out.println("Can't add user");
            return -1;
        }
        int result = -1;
        try {
            Map<Integer, Object> queryArgs = new HashMap<>();
            queryArgs.put(1, user.getName());
            queryArgs.put(2, Integer.toString(user.getPassword().hashCode()));
            queryArgs.put(3, user.getName());
            result = executor.execUpdate(connection, "INSERT INTO USERS (LOGIN, PASSWORD, NICK) " +
                            "VALUES (?, ?, ? )", queryArgs, (r) -> {
                if (r.next()) {
                    return r.getInt(1);
                }
                return -1;
            });
        } catch (Exception e) {
            System.err.println("UserStore: failed to write data " + e.getMessage());
            throw e;
        }
        return result;
    }

    //Returns 1000 first objects
    @Override
    public List<User> getUserList() throws Exception{
        Map<Integer, Object> queryArgs = new HashMap<>();
        return executor.execQuery(connection, "SELECT * FROM USERS LIMIT 10000", queryArgs, (r) -> {
            List<User> data = new ArrayList<User>();
            while (r.next()) {
                User u = new User(r.getString("login"), r.getString("password"), r.getString("nick"));
                int id = r.getInt("id");
                u.setId(id);
                data.add(u);
            }
            return data;
        });
    }
}
