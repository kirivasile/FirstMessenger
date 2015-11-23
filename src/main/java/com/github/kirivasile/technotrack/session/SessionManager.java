package com.github.kirivasile.technotrack.session;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Хранение всех активных сессий
 */
public class SessionManager {
    private Set<Session> sessions;

    public SessionManager() {
        sessions = new HashSet<>();
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public synchronized void addSession(Session session) {
        sessions.add(session);
    }

    /**
     * Выдача сессии по идентфикатору пользователя
     * @param id Идентификатор пользователя
     * @return Сессия для этого пользователя
     */
    public Session getSessionById(int id) {
        for (Session it: sessions) {
            if (it.getCurrentUserId() == id) {
                return it;
            }
        }
        return null;
    }
}
