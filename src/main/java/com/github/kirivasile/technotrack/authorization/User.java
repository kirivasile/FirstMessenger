package com.github.kirivasile.technotrack.authorization;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Класс-информация о пользователе
 */
public class User {
    /**
     * Данные о пользователе
     */
    private String name;
    private String password;
    private String nickname;
    private int id;

    public User() {}

    public User(String name, String password, String nickname) {
        this.name = name;
        this.password = password;
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getNick() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
