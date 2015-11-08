package com.github.kirivasile.technotrack.authorization;


import com.github.kirivasile.technotrack.session.Session;

import java.io.BufferedReader;
import java.io.Console;
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

    public void registerUser(Session session) throws IOException {
        //BufferedReader reader = session.getReader();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String name;
        System.out.println("Signing up. Please enter your name.");
        name = reader.readLine();
        if (name != null) {
            User currentUser = userStore.getUser(name);
            if (currentUser != null) {
                System.out.println("Sorry, but user with this name has already registered");
            } else {
                Console console = System.console();
                String password;
                if (console == null) {
                    //in IDE
                    System.out.print("Password: ");
                    password = reader.readLine();
                } else {
                    //in Console
                    char[] passwd = console.readPassword("[%s]", "Password:");
                    password = passwd.toString();
                }
                userStore.addUser(new User(name, password, name));
                session.setCurrentUserName(name);
                System.out.println("User was successfully signed up");
            }
        } else {
            System.out.println("Incorrect name");
        }
    }

    public void authorizeUser(String name, String password, Session session) throws IOException {
        User user = userStore.getUser(name);
        if (user != null) {
            if (user.getPassword().equals(Integer.toString(password.hashCode()))) {
                session.setCurrentUserName(name);
                System.out.println("Hello, " + name + "!");
            } else {
                System.out.println("Password is incorrect");
            }
        } else {
            System.out.println("Sorry, but we didn't find user with this name: " + name);
        }
    }

    public boolean changeUserNick(String name, String newNickName) {
        User user = userStore.getUser(name);
        if (user == null) {
            return false;
        }
        user.setNickname(newNickName);
        return true;
    }
}
