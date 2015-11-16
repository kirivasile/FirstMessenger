package com.github.kirivasile.technotrack.authorization;


import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.*;

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

    public synchronized void registerUser(String name, String password, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (name != null && password != null) {
            User currentUser = userStore.getUser(name);
            if (currentUser != null) {
                //writer.writeUTF("Sorry, but user with this name has already registered");
                message = "Sorry, but user with this name has already registered";
                success = AnswerMessage.Value.ERROR;
            } else {
                userStore.addUser(new User(name, password, name));
                session.setCurrentUserName(name);
                message = String.format("User was successfully signed up" +
                                                "Login: %s, Password: %s", name, password);
                //writer.writeUTF(message);
                success = AnswerMessage.Value.SUCCESS;
            }
        } else {
            //writer.writeUTF("Incorrect name/password");
            message = "Incorrect name/password";
            success = AnswerMessage.Value.ERROR;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public synchronized void authorizeUser(String name, String password, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        User user = userStore.getUser(name);
        if (user != null) {
            if (user.getPassword().equals(Integer.toString(password.hashCode()))) {
                session.setCurrentUserName(name);
                //writer.writeUTF("Hello, " + name + "!");
                message = "Hello, " + name + "!";
                success = AnswerMessage.Value.SUCCESS;
            } else {
                //writer.writeUTF("Password is incorrect");
                message = "Password is incorrect";
                success = AnswerMessage.Value.ERROR;
            }
        } else {
            //writer.writeUTF("Sorry, but we didn't find user with this name: " + name);
            message = "Sorry, but we didn't find user with this name: " + name;
            success = AnswerMessage.Value.ERROR;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public boolean changeUserNick(String name, String newNickName) {
        User user = userStore.getUser(name);
        if (user == null) {
            return false;
        }
        user.setNickname(newNickName);
        return true;
    }

    public int changePassword(String name, String oldPassword, String newPassword) {
        User user = userStore.getUser(name);
        if (user == null) {
            return -1;
        }
        String userPassword = user.getPassword();
        String oldPasswordHash = Integer.toString(oldPassword.hashCode());
        if (userPassword.equals(oldPasswordHash)) {
            user.setPassword(Integer.toString(newPassword.hashCode()));
            return 0;
        } else {
            return 1;
        }
    }

    public User getUserInfo(String name) {
        return userStore.getUser(name);
    }
}
