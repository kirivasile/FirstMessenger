package com.github.kirivasile.technotrack.authorization;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 29.09.2015.
 */
public class FileUserStore implements AutoCloseable, UserStore {
    /*To reduce the number of writings, I've created a local cache of users, which would be used for reading*/
    private Map<Integer, User> users;
    private File userList;

    public FileUserStore() {
        users = new HashMap<>();
        userList = new File("users.db");
        try {
            if (!userList.exists()) {
                userList.createNewFile();
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(userList.getAbsolutePath()))) {
                readDataFromFile(reader);
            } catch (Exception e) {
                System.err.println("Error in reading from file: " + e.toString());
            }
        } catch (IOException e) {
            System.err.println("IOException in creating file");
        }
    }

    private synchronized void readDataFromFile(BufferedReader reader) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parsedLine = line.split(", ");
            if (parsedLine.length != 4) {
                System.err.println("Incorrect information read");
                return;
            }
            Integer id = Integer.parseInt(parsedLine[0]);
            String name = parsedLine[1];
            String passwordHash = parsedLine[2];
            String nick = parsedLine[3];
            User input = new User(name, passwordHash, nick);
            input.setId(id);
            users.put(id, input);
        }
    }

    private synchronized void writeDataToFile(BufferedWriter writer) throws Exception {
        for (Map.Entry<Integer, User> pair : users.entrySet()) {
            Integer id = pair.getKey();
            String name = pair.getValue().getName();
            String hashPassword = pair.getValue().getPassword();
            String nickname = pair.getValue().getNick();
            writer.write(id + ", " + name + ", " + hashPassword + ", " + nickname + "\n");
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

    public synchronized User getUser(int id) {
        if (id < 0) {
            return null;
        }
        return users.get(id);
    }

    public synchronized int addUser(User user) {
        if (user == null) {
            System.out.println("Can't add user");
            return -1;
        }
        int hash = user.getPassword().hashCode();
        int id = users.size();
        User input = new User(user.getName(), Integer.toString(hash), user.getName());
        input.setId(id);
        users.put(id, input);
        return id;
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userList.getAbsolutePath()))) {
            writeDataToFile(writer);
        } catch (Exception e) {
            System.err.println("Error in writing to file: " + e.toString());
        }
    }
}
