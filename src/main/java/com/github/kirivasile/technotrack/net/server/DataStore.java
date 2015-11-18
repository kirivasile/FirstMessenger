package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.commands.*;
import com.github.kirivasile.technotrack.message.ChatStore;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 09.11.2015.
 */
public class DataStore {
    private UserStore userStore;
    private Map<String, Command> commandsStore;
    private ChatStore chatStore;
    private Connection connection;

    public DataStore(UserStore fileUserStore, ChatStore chatStore, Connection c) {
        this.userStore = fileUserStore;
        this.chatStore = chatStore;
        this.connection = c;
        commandsStore = new HashMap<>();
        commandsStore.put("/help", new HelpCommand());
        commandsStore.put("/login", new LoginCommand());
        commandsStore.put("/user", new UserCommand());
        commandsStore.put("/find", new FindCommand());
        commandsStore.put("/register", new RegisterCommand());
        commandsStore.put("/user_info", new UserInfoCommand());
        commandsStore.put("/user_pass", new ChangePasswordCommand());
        commandsStore.put("/chat_create", new ChatCreateCommand());
        commandsStore.put("/chat_list", new ChatListCommand());
        commandsStore.put("/user_list", new UserListCommand());
        commandsStore.put("/chat_send", new ChatSendCommand());
        commandsStore.put("/chat_history", new ChatHistoryCommand());
    }

    public UserStore getUserStore() {
        return userStore;
    }

    public ChatStore getChatStore() {
        return chatStore;
    }

    public Map<String, Command> getCommandsStore() {
        return commandsStore;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() throws Exception {
    }
}
