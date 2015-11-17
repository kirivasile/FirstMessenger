package com.github.kirivasile.technotrack.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class Chat {
    private Integer id;
    private List<Integer> messageIds;
    private List<Integer> participantIds;

    public Chat(List<Integer> participantIds, Integer id) {
        this.participantIds = participantIds;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Integer> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<Integer> messageIds) {
        this.messageIds = messageIds;
    }

    public List<Integer> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Integer> participantIds) {
        this.participantIds = participantIds;
    }

    public void addParticipant(Integer id) {
        participantIds.add(id);
    }

    public void addMessage(Integer id) {
        messageIds.add(id);
    }
}
