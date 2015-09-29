package ru.mail.track.Vasilev.Kirill;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 29.09.2015.
 */
public class UserStore {
    private Map<String, User> users;

    public UserStore() {
        users = new HashMap<>();
    }

    public User getUser(String name) {
        return users.get(name);
    }
}
