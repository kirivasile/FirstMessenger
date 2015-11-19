package com.github.kirivasile.technotrack.message;

import com.github.kirivasile.technotrack.jdbc.QueryExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Kirill on 18.10.2015.
 */
public class DBMessageStore implements AutoCloseable, MessageStore {
    private Map<Integer, Message> messageMap;
    private Connection connection;

    public DBMessageStore(Connection conn, Chat chat) {
        try {
            this.connection = conn;
            messageMap = new HashMap<>();
            QueryExecutor executor = new QueryExecutor();
            int chatId = chat.getId();
            String sql = String.format("SELECT * FROM message WHERE chat_id = %d", chatId);
            executor.execQuery(connection, sql, (r) -> {
                while (r.next()) {
                    int authorId = r.getInt("author_id");
                    String value = r.getString("value");
                    int messageId = r.getInt("id");
                    Message message = new Message();
                    message.setAuthorId(authorId);
                    message.setMessage(value);
                    messageMap.put(messageId, message);
                }
                return null;
            });
        } catch (Exception e) {
            System.err.println("MessageStore: failed to read data " + e.getMessage());
        }
    }

    public synchronized void addMessage(int authorId, String authorName, String value, Chat chat) {
        int messageId = -1;
        try {
            if (value.contains("\'")) {
                value = value.replace('\'', ' ');
            }
            Statement stmt = connection.createStatement();
            String sql = String.format("INSERT INTO message (author_id, value, chat_id) VALUES (%d, \'%s\', %d)",
                            authorId, value, chat.getId());
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                messageId = rs.getInt(1);
            }
            Message message = new Message();
            message.setAuthorId(authorId);
            message.setMessage(value);
            message.setId(messageId);
            messageMap.put(messageId, message);
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println("MessageStore: failed to write data " + e.getMessage());
        }
    }

    @Override
    public synchronized Map<Integer, Message> getMessagesMap() {
        return messageMap;
    }

    @Override
    public synchronized void close() {
    }
}
