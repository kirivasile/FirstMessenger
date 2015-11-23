package com.github.kirivasile.technotrack.message;

import com.github.kirivasile.technotrack.jdbc.QueryExecutor;

import java.sql.Connection;
import java.util.*;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Реализация хранилища сообщений на БД PostegreSQL
 */
public class DBMessageStore implements MessageStore {
    private Connection connection;
    private int chatId;
    private QueryExecutor executor;

    public DBMessageStore(Connection conn, Chat chat) {
        this.connection = conn;
        this.chatId = chat.getId();
        this.executor = new QueryExecutor();
    }

    /**
     * @see MessageStore#addMessage(int, String, String, Chat)
     */
    @Override
    public synchronized void addMessage(int authorId, String authorName, String value, Chat chat) throws Exception {
        try {
            if (value.contains("\'")) {
                value = value.replace('\'', ' ');
            }
            String sql = "INSERT INTO message (author_id, value, chat_id) VALUES (? ,? ,?)";
            Map<Integer, Object> queryArgs = new HashMap<>();
            queryArgs.put(1, authorId);
            queryArgs.put(2, value);
            queryArgs.put(3, chat.getId());
            executor.execUpdate(connection, sql, queryArgs);
        } catch (Exception e) {
            System.err.println("MessageStore: failed to write data " + e.getMessage());
        }
    }

    /**
     * @see MessageStore#getMessagesMap()
     */
    @Override
    public synchronized Map<Integer, Message> getMessagesMap() throws Exception {
        Map<Integer, Message> messageMap = new HashMap<>();
        String sql = "SELECT * FROM message WHERE chat_id = ? LIMIT 10000";
        Map<Integer, Object> queryArgs = new HashMap<>();
        queryArgs.put(1, chatId);
        executor.execQuery(connection, sql, queryArgs, (r) -> {
            while (r.next()) {
                int authorId = r.getInt("author_id");
                String value = r.getString("value");
                int messageId = r.getInt("id");
                Message message = new Message();
                message.setAuthorId(authorId);
                message.setMessage(value);
                message.setId(messageId);
                messageMap.put(messageId, message);
            }
            return null;
        });
        return messageMap;
    }

    /**
     * @see MessageStore#getMessageByRegex(String)
     */
    @Override
    public List<Message> getMessageByRegex(String regex) throws Exception{
        List<Message> messages = new ArrayList<>();
        String sql = String.format("SELECT * FROM message WHERE chat_id = %d AND value LIKE \'%s\'", chatId, regex);
        executor.execQuery(connection, sql, (r) -> {
            while (r.next()) {
                int authorId = r.getInt("author_id");
                String value = r.getString("value");
                int messageId = r.getInt("id");
                Message message = new Message();
                message.setAuthorId(authorId);
                message.setMessage(value);
                message.setId(messageId);
                messages.add(message);
            }
            return null;
        });
        return messages;
    }
}
