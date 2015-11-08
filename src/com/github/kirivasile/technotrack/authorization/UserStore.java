package com.github.kirivasile.technotrack.authorization;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 29.09.2015.
 */
public class UserStore implements AutoCloseable {
    /*To reduce the number of writings, I've created a local cache of users, which would be used for reading*/
    private Map<String, User> users;
    private File userList;

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
                    if (parsedLine.length != 3) {
                        System.err.println("Incorrect information read");
                        return;
                    }
                    String name = parsedLine[0];
                    String passwordHash = parsedLine[1];
                    String nick = parsedLine[2];
                    users.put(name, new User(name, passwordHash, nick));
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

    public boolean removeUser(String name, User user) {
        return users.remove(name, user);
    }

    public void addUser(User user) {
        if (user == null) {
            System.out.println("Can't add user");
            return;
        }
        int hash = user.getPassword().hashCode();
        User input = new User(user.getName(), Integer.toString(hash), user.getName());
        users.put(user.getName(), input);
    }

    @Override
    public void close() throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userList.getAbsolutePath()))) {
            for (Map.Entry<String, User> pair : users.entrySet()) {
                String name = pair.getKey();
                String hashPassword = pair.getValue().getPassword();
                String nickname = pair.getValue().getNick();
                writer.write(name + ", " + hashPassword + ", " + nickname + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error in writing to file: " + e.toString());
        }
    }

}
