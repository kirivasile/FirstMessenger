package ru.mail.track.Vasilev.Kirill;

/**
 * Created by Kirill on 29.09.2015.
 */
public class User {
    private String name, password;

    public User() {}

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
