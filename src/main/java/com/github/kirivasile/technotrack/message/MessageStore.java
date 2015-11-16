package com.github.kirivasile.technotrack.message;

import java.util.Map;

/**
 * Created by Kirill on 09.11.2015.
 */
public interface MessageStore {
    void addMessage(String from, String message);
    Map<Integer, ChatMessage> getMessagesMap();
    void close() throws Exception;
}
