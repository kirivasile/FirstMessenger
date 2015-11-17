package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.commands.*;
import com.github.kirivasile.technotrack.message.ChatStore;
import com.github.kirivasile.technotrack.message.MessageStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 09.11.2015.
 */
public class DataStore {
    private UserStore userStore;
    private MessageStore messageStore;
    private Map<String, Command> commandsStore;
    private ChatStore chatStore;

    public DataStore(UserStore fileUserStore, MessageStore messageStore, ChatStore chatStore) {
        this.userStore = fileUserStore;
        this.messageStore = messageStore;
        this.chatStore = chatStore;
        commandsStore = new HashMap<>();
        commandsStore.put("/help", new HelpCommand());
        commandsStore.put("/login", new LoginCommand());
        commandsStore.put("/user", new UserCommand());
        commandsStore.put("/message", new HistoryCommand());
        commandsStore.put("/find", new FindCommand());
        commandsStore.put("/register", new RegisterCommand());
        commandsStore.put("/user_info", new UserInfoCommand());
        commandsStore.put("/user_pass", new ChangePasswordCommand());
        commandsStore.put("/chat_create", new ChatCreateCommand());
        commandsStore.put("/chat_list", new ChatListCommand());
        commandsStore.put("/user_list", new UserListCommand());
    }

    public UserStore getUserStore() {
        return userStore;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public ChatStore getChatStore() {
        return chatStore;
    }

    public Map<String, Command> getCommandsStore() {
        return commandsStore;
    }

    public void close() throws Exception {
        userStore.close();
        messageStore.close();

    }
}
