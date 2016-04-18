package com.github.kirivasile.technotrack.session;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.message.ChatStore;
import com.github.kirivasile.technotrack.message.MessageStore;
import com.github.kirivasile.technotrack.net.server.DataStore;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by Kirill on 18.10.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Хранение данных о текущей сессии с клиентом
 */
public class Session {
    /**
     * Потоки для общения с клиентом
     */
    private DataInputStream reader;
    private DataOutputStream writer;

    /**
     * Сервис авторизации и регистрации клиентов
     */
    private AuthorizationService authorizationService;

    /**
     * Имя текущего пользователя
     */
    private String currentUserName;

    /**
     * @see DataStore
     */
    private DataStore dataStore;

    /**
     * Идентификатор текущего пользователя
     */
    private int currentUserId;

    /**
     * @see SessionManager
     */
    private SessionManager sessionManager;

    public Session(DataInputStream reader, DataOutputStream writer,
                   AuthorizationService authService, DataStore dataStore, SessionManager manager) {
        this.reader = reader;
        this.writer = writer;
        this.authorizationService = authService;
        this.dataStore = dataStore;
        this.sessionManager = manager;
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

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(int currentUserId) {
        this.currentUserId = currentUserId;
    }
}
