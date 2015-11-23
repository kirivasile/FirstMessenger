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
public class DBUserStore implements AutoCloseable, UserStore {
    /*To reduce the number of writings, I've created a local cache of users, which would be used for reading*/
    private Map<Integer, User> users;
    private Connection connection;
    private PreparedStatement insertStatement;

    public DBUserStore(Connection conn) {
        try {
            this.connection = conn;
            users = new HashMap<>();
            QueryExecutor executor = new QueryExecutor();
            List<User> userList = executor.execQuery(connection, "SELECT * FROM USERS", (r) -> {
                List<User> data = new ArrayList<>();
                while (r.next()) {
                    User u = new User(r.getString("login"), r.getString("password"), r.getString("nick"));
                    int id = r.getInt("id");
                    u.setId(id);
                    data.add(u);
                }
                return data;
            });
            for (User it : userList) {
                users.put(it.getId(), it);
            }

            String insertSql = "INSERT INTO USERS (LOGIN, PASSWORD, NICK) VALUES (?, ?, ? )";
            insertStatement = conn.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS);
        } catch (Exception e) {
            System.err.println("UserStore: failed to open database " + e.getMessage());
        }
    }

    public synchronized User getUserByName(String name) {
        if (name == null) {
            return null;
        }
        for (Map.Entry<Integer, User> pair : users.entrySet()) {
            if (pair.getValue().getName().equals(name)) {
                return pair.getValue();
            }
        }
        return null;
    }

    @Override
    public synchronized User getUser(int id) {
        if (id < 0) {
            return null;
        }
        return users.get(id);
    }

    @Override
    public synchronized int addUser(User user) throws Exception{
        if (user == null) {
            System.out.println("Can't add user");
            return -1;
        }
        int result = -1;
        try {
            /*stmt.executeUpdate(String.format("INSERT INTO USERS (LOGIN, PASSWORD, NICK) " +
                            "VALUES (\'%s\', \'%s\', \'%s\' )", user.getName(),
                    Integer.toString(user.getPassword().hashCode()), user.getName()), Statement.RETURN_GENERATED_KEYS);*/
            insertStatement.setString(1, user.getName());
            insertStatement.setString(2, Integer.toString(user.getPassword().hashCode()));
            insertStatement.setString(3, user.getName());
            insertStatement.executeUpdate();
            ResultSet rs = insertStatement.getGeneratedKeys();
            while (rs.next()) {
                result = rs.getInt(1);
            }
            int hash = user.getPassword().hashCode();
            User input = new User(user.getName(), Integer.toString(hash), user.getName());
            input.setId(result);
            users.put(result, input);
        } catch (Exception e) {
            System.err.println("UserStore: failed to write data " + e.getMessage());
            throw new Exception("Failed to add user");
        }
        return result;
    }

    @Override
    public List<User> getUserList() {
        List<User> result = new ArrayList<>();
        for (Map.Entry<Integer, User> user : users.entrySet()) {
            result.add(user.getValue());
        }
        return result;
    }

    @Override
    public synchronized void close() throws Exception {
    }
}
