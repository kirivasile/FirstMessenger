package com.github.kirivasile.technotrack.history;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by Kirill on 09.11.2015.
 */
public interface MessageStore {
    void addMessage(String from, String message);
    Map<Calendar, Message> getMessagesMap();
    void close() throws Exception;
}
