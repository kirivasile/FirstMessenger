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

/**
 * Реализация хранилища чатов в БД PostegreSql
 */
public class DBChatStore implements ChatStore {
    private Connection connection;
    private QueryExecutor executor;

    public DBChatStore(Connection conn) {
        this.connection = conn;
        this.executor = new QueryExecutor();
    }

    /**
     * @see ChatStore#createChat(List)
     */
    @Override
    public synchronized int createChat(List<Integer> participants) throws Exception {
        int chatId = -1;
        String sql = "INSERT INTO chats (temp) VALUES (?)";
        Map<Integer, Object> queryArgs = new HashMap<>();
        queryArgs.put(1, "temp");
        chatId = executor.execUpdate(connection, sql, queryArgs, (r) -> {
            if (r.next()) {
                return r.getInt(1);
            }
            return -1;
        });
        sql = "INSERT INTO userschat (user_id, chat_id) VALUES (?, ?)";
        for (Integer userId : participants) {
            queryArgs.clear();
            queryArgs.put(1, userId);
            queryArgs.put(2, chatId);
            executor.execUpdate(connection, sql, queryArgs);
        }
        return chatId;
    }

    /**
     * @see ChatStore#getChatList()
     */
    @Override
    public Map<Integer, Chat> getChatList() throws Exception {
        String sql = "SELECT * FROM userschat LIMIT 10000";
        return executor.execQuery(connection, sql, (r) -> {
            Map<Integer, Chat> result = new HashMap<>();
            while (r.next()) {
                int chatId = r.getInt("chat_id");
                int userId = r.getInt("user_id");
                Chat chat = result.get(chatId);
                if (chat == null) {
                    chat = new Chat(chatId, connection);
                    chat.addParticipant(userId);
                    result.put(chatId, chat);
                } else {
                    chat.addParticipant(userId);
                }
            }
            return result;
        });
    }

    /**
     * @see ChatStore#getChat(Integer)
     */
    @Override
    public Chat getChat(Integer id) throws Exception {
        Chat result = new Chat(id, connection);
        String sql = "SELECT * FROM userschat WHERE chat_id = ?";
        Map<Integer, Object> queryArgs = new HashMap<>();
        queryArgs.put(1, id);
        executor.execQuery(connection, sql, queryArgs, (r) -> {
            while (r.next()) {
                int userId = r.getInt("user_id");
                result.addParticipant(userId);
            }
            return null;
        });
        return result;
    }
}
