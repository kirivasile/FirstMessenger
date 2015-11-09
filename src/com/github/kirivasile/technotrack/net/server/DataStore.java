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
        commandsStore.put(new HelpCommand().toString(), new HelpCommand());
        commandsStore.put(new LoginCommand().toString(), new LoginCommand());
        commandsStore.put(new UserCommand().toString(), new UserCommand());
        commandsStore.put(new HistoryCommand().toString(), new HistoryCommand());
        commandsStore.put(new FindCommand().toString(), new FindCommand());
        commandsStore.put(new RegisterCommand().toString(), new RegisterCommand());
        commandsStore.put(new UserInfoCommand().toString(), new UserInfoCommand());
        commandsStore.put(new ChangePasswordCommand().toString(), new ChangePasswordCommand());
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
