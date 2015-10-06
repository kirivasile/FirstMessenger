package com.github.kirivasile.technotrack;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 29.09.2015.
 */
public class UserStore {
    /*To reduce the number of writings, I've created a local cache of users, which would be used for reading*/
    private Map<String, User> bufferedUsers;
    String filePath;
    File userDb;

    public UserStore() {
        bufferedUsers = new HashMap<>();
        filePath = "users.db";
        userDb = new File(filePath);
        try {
            if (!userDb.exists()) {
                userDb.createNewFile();
            }
            try (FileInputStream reader = new FileInputStream(userDb.getAbsolutePath())) {
                if (reader.available() == 0) {
                    return;
                }
                ObjectInputStream objReader = new ObjectInputStream(reader);
                while (reader.available() > 0) {
                    User buf = (User)objReader.readObject();
                    bufferedUsers.put(buf.getName(), buf);
                }
            }
            catch (Exception e) {
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
        return bufferedUsers.get(name);
    }

    public void addUser(User user) {
        if (user == null) {
            System.out.println("Can't add user");
            return;
        }
        bufferedUsers.put(user.getName(), user);
        try (FileOutputStream writer = new FileOutputStream(userDb.getAbsolutePath())) {
            ObjectOutputStream objWriter = new ObjectOutputStream(writer);
            for (Map.Entry<String, User> pair : bufferedUsers.entrySet()) {
                objWriter.writeObject(pair.getValue());
            }
        } catch (IOException e) {
            System.err.println("IOException in writing to file " + e.getMessage());
        }
    }
}
