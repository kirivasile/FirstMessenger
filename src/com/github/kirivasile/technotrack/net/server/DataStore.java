package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.history.MessageStore;

/**
 * Created by Kirill on 09.11.2015.
 */
public class DataStore {
    private UserStore userStore;
    private MessageStore messageStore;

    public DataStore(UserStore userStore, MessageStore messageStore) {
        this.userStore = userStore;
        this.messageStore = messageStore;
    }

    public UserStore getUserStore() {
        return userStore;
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }

    public void close() throws Exception {
        userStore.close();
        messageStore.close();
    }
}
