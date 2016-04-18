package com.github.kirivasile.technotrack.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Обработчик результата запроса с БД
 */
public interface ResultHandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
