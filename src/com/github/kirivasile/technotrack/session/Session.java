package com.github.kirivasile.technotrack.session;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.history.MessageStore;

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
    private MessageStore messageStore;

    public Session(DataInputStream r, DataOutputStream w, AuthorizationService as, MessageStore h) {
        this.reader = r;
        this.writer = w;
        this.authorizationService = as;
        this.messageStore = h;
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

    public MessageStore getMessageStore() {
        return messageStore;
    }
}
