package com.github.kirivasile.technotrack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Kirill on 29.09.2015.
 */
public class AuthorizationService {
    private UserStore userStore;

    private AuthorizationService() {
    }

    public AuthorizationService(UserStore userStore) {
        this.userStore = userStore;
    }

    public void run() {
        String name;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Please enter your name. If you want to exit, type \"EXIT\"");
                name = reader.readLine();
                if (name != null) {
                    if (name.equals("EXIT")) {
                        break;
                    }
                    User currentUser = userStore.getUser(name);
                    if (currentUser != null) {
                        String password;
                        System.out.println("User found. Please enter your password");
                        password = reader.readLine();
                        if (currentUser.checkPassword(password)) {
                            System.out.println("Password is correct");
                        } else {
                            System.out.println("Password is incorrect");
                        }
                    } else {
                        System.out.println("Hello, " + name + "! Please enter your password");
                        String password = reader.readLine();
                        if (password != null) {
                            userStore.addUser(new User(name, password));
                            System.out.println("New user was succesfully added!");
                        } else {
                            System.out.println("Incorrect password");
                        }
                    }
                } else {
                    System.out.println("Incorrect name");
                }
            }
        } catch (IOException e) {
            System.err.println("IOException in opening reader");
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception");
        }
    }
}
