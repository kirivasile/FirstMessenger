package com.github.kirivasile.technotrack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 29.09.2015.
 */
public class UserStore {
    /*To reduce the number of writings, I've created a local cache of users, which would be used for reading*/
    private Map<String, User> users;
    File userList;

    public UserStore() {
        users = new HashMap<>();
        userList = new File("users.db");
        try {
            if (!userList.exists()) {
                userList.createNewFile();
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(userList.getAbsolutePath()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parsedLine = line.split(", ");
                    if (parsedLine.length != 2) {
                        System.err.println("Incorrect information read");
                        return;
                    }
                    String name = parsedLine[0];
                    String passwordHash = parsedLine[1];
                    users.put(name, new User(name, passwordHash));
                }
            } catch (Exception e) {
                System.err.println("Error in reading from file: " + e.toString());
            }
        } catch (IOException e) {
            System.err.println("IOException in creating file");
        }
    }

    public User getUser(String name) {
        if (name == null) {
            return null;
        }
        return users.get(name);
    }

    public void addUser(User user) {
        if (user == null) {
            System.out.println("Can't add user");
            return;
        }
        int hash = user.getPassword().hashCode();
        User input = new User(user.getName(), Integer.toString(hash));
        users.put(user.getName(), input);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userList.getAbsolutePath()))) {
            for (Map.Entry<String, User> pair : users.entrySet()) {
                writer.write(pair.getKey() + ", " + pair.getValue().getPassword() + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error in writing to file: " + e.toString());
        }
    }
}
