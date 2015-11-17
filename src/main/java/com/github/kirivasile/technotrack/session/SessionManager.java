package com.github.kirivasile.technotrack.session;

import java.util.Set;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class SessionManager {
    private Set<Session> sessions;

    public SessionManager(Set<Session> sessions) {
        this.sessions = sessions;
    }

    public Set<Session> getSessions() {
        return sessions;
    }

    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }
}
