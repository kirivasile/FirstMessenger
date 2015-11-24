package com.github.kirivasile.technotrack.authorization;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Хранилище пользователей
 */
public interface UserStore {
    /**
     * Вернуть пользователя по идентификатору
     * @param id Идентификатор
     * @return Класс-информация о пользователе
     * @throws Exception
     */
    User getUser(int id) throws Exception;

    /**
     * Вернуть пользователя по логину
     * @param name Логин
     * @return Список пользователей с таким логином. Размер должен быть 1.
     * @throws Exception
     */
    List<User> getUserByName(String name) throws Exception;

    /**
     * Добавить пользователя в БД
     * @param user Класс-информация о пользователе
     * @return Идентификатор пользователя
     * @throws Exception
     */
    int addUser(User user) throws Exception;

    /**
     * Вернуть 10000 пользователей. Константа произвольная. Выбрана для того, если число пользователей
     * будет очень огромно
     * @return Список пользователей
     * @throws Exception
     */
    List<User> getUserList() throws Exception;

    /**
     * Закрыть открытые ресурсы
     * @throws SQLException
     */
    void close() throws SQLException;
}
