package com.github.kirivasile.technotrack.session;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.message.MessageStore;

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
    private MessageStore fileMessageStore;

    public Session(DataInputStream reader, DataOutputStream writer,
                   AuthorizationService authService, MessageStore messageStore) {
        this.reader = reader;
        this.writer = writer;
        this.authorizationService = authService;
        this.fileMessageStore = messageStore;
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

    public MessageStore getFileMessageStore() {
        return fileMessageStore;
    }
}
