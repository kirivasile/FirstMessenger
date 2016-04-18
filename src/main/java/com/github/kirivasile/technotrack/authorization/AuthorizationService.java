package com.github.kirivasile.technotrack.authorization;


import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.*;
import java.util.List;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Класс для авторизации и регистрации пользователей
 */
public class AuthorizationService {
    /**
     * Хранилище пользователей.
     */
    private UserStore userStore;

    private AuthorizationService() {
    }

    public AuthorizationService(UserStore userStore) {
        this.userStore = userStore;
    }

    /**
     * Регистрирует пользователя со следующими данными
     * @param name Логин
     * @param password Пароль
     * @param session Данные о текущей сессии
     * @throws Exception
     */
    public synchronized void registerUser(String name, String password, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (name != null && password != null) {
            List<User> userByName = userStore.getUserByName(name);
            if (userByName.size() > 0) {
                message = "Sorry, but user with this name has already registered";
                success = AnswerMessage.Value.ERROR;
            } else {
                int id = userStore.addUser(new User(name, password, name));
                session.setCurrentUserName(name);
                session.setCurrentUserId(id);
                message = String.format("User was successfully signed up" +
                                                "Login: %s, Password: %s, Id: %d", name, password, id);
                success = AnswerMessage.Value.SUCCESS;
            }
        } else {
            message = "Incorrect name/password";
            success = AnswerMessage.Value.ERROR;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }


    /**
     * Авторизует пользователя со следующими данными
     * @param name Логин
     * @param password Пароль
     * @param session Данные о текущей сессии
     * @throws Exception
     */
    public synchronized void authorizeUser(String name, String password, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        List<User> userByName = userStore.getUserByName(name);
        if (userByName.size() == 1) {
            User user = userByName.get(0);
            if (user.getPassword().equals(Integer.toString(password.hashCode()))) {
                session.setCurrentUserName(name);
                session.setCurrentUserId(user.getId());
                message = String.format("Hello, %s! Your id = %d", name, user.getId());
                success = AnswerMessage.Value.SUCCESS;
            } else {
                message = "Password is incorrect";
                success = AnswerMessage.Value.ERROR;
            }
        } else {
            message = "Sorry, but we didn't find user with this name: " + name;
            success = AnswerMessage.Value.ERROR;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }


    /**
     * Меняет ник пользователя. (Присутствовало в старом ТЗ)
     * @param id Идентификатор пользователя
     * @param newNickName Новый ник
     * @return Возвращает индикатор успешного выполнения метода
     * @throws Exception
     */
    public boolean changeUserNick(int id, String newNickName) throws Exception {
        User user = userStore.getUser(id);
        if (user == null) {
            return false;
        }
        user.setNickname(newNickName);
        return true;
    }


    /**
     * Меняет пароль пользователя
     * @param id Идентификатор пользователя
     * @param oldPassword Старый пароль
     * @param newPassword Новый пароль
     * @return Возвращает результат выполнения метода: 1 - пароль не совпадает, 0 - успешное выполнение,
     *         -1 - отсутствие такого пользователя
     * @throws Exception
     */
    public int changePassword(int id, String oldPassword, String newPassword) throws Exception {
        User user = userStore.getUser(id);
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

    /**
     * Возвращает класс User
     * @param id Идентификатор пользователя
     * @return Сам класс User
     * @throws Exception
     */
    public User getUserInfo(int id) throws Exception  {
        return userStore.getUser(id);
    }
}
