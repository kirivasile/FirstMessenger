package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.commands.*;
import com.github.kirivasile.technotrack.history.MessageStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 09.11.2015.
 */
public class DataStore {
    private UserStore userStore;
    private MessageStore messageStore;
    private Map<String, Command> commandsStore;

    public DataStore(UserStore fileUserStore, MessageStore messageStore) {
        this.userStore = fileUserStore;
        this.messageStore = messageStore;
        commandsStore = new HashMap<>();
        commandsStore.put("/help", new HelpCommand());
        commandsStore.put("/login", new LoginCommand());
        commandsStore.put("/user", new UserCommand());
        commandsStore.put("/history", new HistoryCommand());
        commandsStore.put("/find", new FindCommand());
        commandsStore.put("/register", new RegisterCommand());
        commandsStore.put("/user_info", new UserInfoCommand());
        commandsStore.put("/user_pass", new ChangePasswordCommand());
    }

    public UserStore getUserStore() {
        return userStore;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public Map<String, Command> getCommandsStore() {
        return commandsStore;
    }

    public void close() throws Exception {
        userStore.close();
        messageStore.close();
    }
}
