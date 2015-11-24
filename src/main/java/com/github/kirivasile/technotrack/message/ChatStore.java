package com.github.kirivasile.technotrack.message;

import com.github.kirivasile.technotrack.message.Chat;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Интерфейс хранилища чатов
 */
public interface ChatStore {
    /**
     * Создать чат
     * @param participants Список участников
     * @return Идентификатор чата
     * @throws Exception
     */
    int createChat(List<Integer> participants) throws Exception;

    /**
     * Выдать список чатов(Не больше 10000)
     * @return Список чатов
     * @throws Exception
     */
    Map<Integer, Chat> getChatList() throws Exception;

    /**
     * Выдать определённый чат по идентификатору
     * @param id Идентификатор чата
     * @return Класс-информация о чате
     * @throws Exception
     */
    Chat getChat(Integer id) throws Exception;

    /**
     * Закрыть открытые ресурсы
     * @throws SQLException
     */
    void close() throws SQLException;
}
