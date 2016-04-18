package com.github.kirivasile.technotrack.message;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Класс, отвечающий за отдельный чат
 */
public class Chat {
    /**
     * Идентификатор чата
     */
    private Integer id;

    /**
     * Хранилище сообщений
     */
    private MessageStore messageStore;

    /**
     * Список участников чата
     */
    private List<Integer> participantIds;

    /**
     * Соединение с БД
     */
    private Connection connection;

    public Chat(Integer id, Connection conn) {
        this.id = id;
        this.participantIds = new ArrayList<>();
        this.connection = conn;
        this.messageStore = new DBMessageStore(connection, this);
    }

    public Chat(List<Integer> participantIds, Integer id, Connection conn) {
        this.id = id;
        this.participantIds = participantIds;
        this.connection = conn;
        this.messageStore = new DBMessageStore(conn, this);
    }

    /**
     * Добавить пользователя к чату
     * @param id Иденификатор пользователя
     * @return Не присутствовал ли пользователь до этого в чате
     */
    public boolean addParticipant(int id) {
        if (participantIds.contains(id)) {
            return false;
        }
        participantIds.add(id);
        return true;
    }

    /**
     * @see MessageStore#addMessage(int, String, String, Chat)
     */
    public void addMessage(int authorId, String authorName, String message) throws Exception {
        messageStore.addMessage(authorId, authorName, message, this);
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

    public Map<Integer, Message> getMessageMap() throws Exception {
        return messageStore.getMessagesMap();
    }

    public MessageStore getMessageStore() {
        return messageStore;
    }
}
