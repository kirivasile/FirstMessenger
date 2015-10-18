package com.github.kirivasile.technotrack;


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

    /*public void run(BufferedReader reader) {
        String command;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Hello. Do you want to sign up or sign in? Type IN or UP. ");
            command = reader.readLine();
            if (command.equals("UP")) {
                registerUser(reader);
            } else if (command.equals("IN")) {
                authorizeUser(reader);
            } else {
                System.out.println("Wrong command.");
            }
        } catch (IOException e) {
            System.err.println("IOException in opening reader");
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.toString());
        }

    }*/

    public void registerUser(BufferedReader reader) throws IOException {
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
                userStore.addUser(new User(name, password));
                System.out.println("User was successfully signed up");
                /*CommandHandler commandHandler = new CommandHandler();
                commandHandler.run(reader);*/
            }
        } else {
            System.out.println("Incorrect name");
        }
    }

    public void authorizeUser(String name, String password) throws IOException {
        User currentUser = userStore.getUser(name);
        if (currentUser != null) {
            /*System.out.println("User found. ");
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
            }*/
            if (currentUser.getPassword().equals(Integer.toString(password.hashCode()))) {
                System.out.println("Hello, " + name + "!");
                /*CommandHandler commandHandler = new CommandHandler();
                commandHandler.run(reader);*/
            } else {
                System.out.println("Password is incorrect");
            }
        } else {
            System.out.println("Sorry, but we didn't find user with this name: " + name);
        }
    }
}
