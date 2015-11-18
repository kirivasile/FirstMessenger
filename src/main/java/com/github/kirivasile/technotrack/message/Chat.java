package com.github.kirivasile.technotrack.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class Chat {
    private Integer id;
    private MessageStore messageStore;
    private List<Integer> participantIds;

    public Chat( Integer id) {
        this.id = id;
        participantIds = new ArrayList<>();
        /*StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(" ");
        builder.append(participantIds.size());
        builder.append(" ");
        for (Integer it : participantIds) {
            builder.append(it);
            builder.append(" ");
        }
        String chatData = builder.toString();
        this.messageStore = new FileMessageStore(String.format("Chat%d", id), chatData);*/
    }

    public Chat(List<Integer> participantIds, Integer id) {
        this.id = id;
        this.participantIds = participantIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Integer> participantIds) {
        this.participantIds = participantIds;
    }

    public boolean addParticipant(int id) {
        if (participantIds.contains(id)) {
            return false;
        }
        participantIds.add(id);
        return true;
    }

    public void addMessage(int authorId, String authorName, String message) {
        messageStore.addMessage(authorId, authorName, message);
    }

    public Map<Integer, ChatMessage> getMessageMap() {
        return messageStore.getMessagesMap();
    }

    public void close() throws Exception {
        messageStore.close();
    }
}
