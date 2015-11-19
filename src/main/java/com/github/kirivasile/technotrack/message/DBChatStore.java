package com.github.kirivasile.technotrack.message;

import com.github.kirivasile.technotrack.jdbc.QueryExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class DBChatStore implements ChatStore {
    private Map<Integer, Chat> chats;
    private Connection connection;

    public DBChatStore(Connection conn) {
        chats = new TreeMap<>();
        this.connection = conn;
        try {
            QueryExecutor executor = new QueryExecutor();
            executor.execQuery(connection, "SELECT user_id, chat_id FROM userschat", (r) -> {
                while (r.next()) {
                    int chatId = r.getInt("chat_id");
                    int userId = r.getInt("user_id");
                    Chat chat = chats.get(chatId);
                    if (chat == null) {
                        chat = new Chat(chatId, conn);
                        chat.addParticipant(userId);
                        chats.put(chatId, chat);
                    } else {
                        chat.addParticipant(userId);
                    }
                }
                return null;
            });
        } catch (Exception e) {
            System.err.println("ChatStore: failed to read data " + e.getMessage());
        }
    }

    @Override
    public synchronized int createChat(List<Integer> participants) throws Exception {
        int chatId = -1;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("INSERT INTO chats (temp) VALUES (\'temp\')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                chatId = rs.getInt(1);
            }
            Chat chat = new Chat(participants, chatId, connection);
            chats.put(chatId, chat);
            for (Integer userId : participants) {
                String sql = String.format("INSERT INTO userschat (user_id, chat_id) VALUES (%d, %d)",userId, chatId);
                stmt.executeUpdate(sql);
            }
        } catch (Exception e) {
            System.err.println("ChatStore: failed to write data " + e.getMessage());
        }
        return chatId;
    }

    @Override
    public Map<Integer, Chat> getChatList() throws Exception {
        return chats;
    }

    @Override
    public Chat getChat(Integer id) throws Exception {
        return chats.get(id);
    }

    @Override
    public void close() throws Exception {
    }
}
