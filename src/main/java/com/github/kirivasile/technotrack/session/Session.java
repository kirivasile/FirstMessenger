package com.github.kirivasile.technotrack.session;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.message.ChatStore;
import com.github.kirivasile.technotrack.message.MessageStore;
import com.github.kirivasile.technotrack.net.server.DataStore;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by Kirill on 18.10.2015.
 */
public class Session {
    private DataInputStream reader;
    private DataOutputStream writer;
    private AuthorizationService authorizationService;
    private String currentUserName;
    private DataStore dataStore;
    private int currentUserId;

    public Session(DataInputStream reader, DataOutputStream writer,
                   AuthorizationService authService, DataStore dataStore) {
        this.reader = reader;
        this.writer = writer;
        this.authorizationService = authService;
        this.dataStore = dataStore;
        this.currentUserId = -1;
    }

    public DataInputStream getReader() {
        return reader;
    }

    public DataOutputStream getWriter() {
        return writer;
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }
}
