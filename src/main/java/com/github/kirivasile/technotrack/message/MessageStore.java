package com.github.kirivasile.technotrack.message;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Интерфейс хранилища сообщений
 */
public interface MessageStore {
    /**
     * Добавить сообщение
     * @param authorId Идентификатор автора
     * @param from Логин автора
     * @param message Сообщение
     * @param chat Чат, в который добавляется это сообщение
     * @throws Exception
     */
    void addMessage(int authorId, String from, String message, Chat chat) throws Exception;

    /**
     * Вернуть список сообщений (Не больше 10000). Нужно для команды /chat_history
     * @return Список сообщений
     * @throws Exception
     */
    Map<Integer, Message> getMessagesMap() throws Exception;

    /**
     * Выдать сообщения по регулярному выражению
     * @param regex Регулярное выражение
     * @return Список сообщений
     * @throws Exception
     */
    List<Message> getMessageByRegex(String regex) throws Exception;

    /**
     * Закрыть открытые ресурсы
     * @throws SQLException
     */
    void close() throws SQLException;
}
