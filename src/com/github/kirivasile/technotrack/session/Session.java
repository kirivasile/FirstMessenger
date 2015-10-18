package com.github.kirivasile.technotrack.session;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;

import java.io.BufferedReader;

/**
 * Created by Kirill on 18.10.2015.
 */
public class Session {
    private BufferedReader reader;
    private AuthorizationService authorizationService;
    private String currentUserName;

    public Session(BufferedReader reader, AuthorizationService authorizationService) {
        this.reader = reader;
        this.authorizationService = authorizationService;
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
}
