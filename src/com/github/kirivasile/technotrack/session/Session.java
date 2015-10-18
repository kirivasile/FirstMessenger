package com.github.kirivasile.technotrack.session;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.history.History;

import java.io.BufferedReader;

/**
 * Created by Kirill on 18.10.2015.
 */
public class Session {
    private BufferedReader reader;
    private AuthorizationService authorizationService;
    private String currentUserName;
    private History history;

    public Session(BufferedReader reader, AuthorizationService authorizationService, History history) {
        this.reader = reader;
        this.authorizationService = authorizationService;
        this.history = history;
    }

    public BufferedReader getReader() {
        return reader;
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

    public History getHistory() {
        return history;
    }
}
