package com.github.kirivasile.technotrack.jdbc;
import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Класс для отправки запросов в БД
 */
public class QueryExecutor {
    /**
     * Словарь подготовленных запросов
     */
    private Map<String, PreparedStatement> preparedStatementMap;

    /**
     * Словарь подготовленных запросов, с Generated Keys
     */
    private Map<String, PreparedStatement> preparedStatementMapGK;

    public QueryExecutor() {
        preparedStatementMap = new HashMap<>();
        preparedStatementMapGK = new HashMap<>();
    }

    /**
     * Подготовить preparedStatement
     * @param connection Соединение с БД
     * @param query SQL-запрос
     * @throws SQLException
     */
    public void prepareStatement(Connection connection, String query) throws SQLException {
        if (preparedStatementMap.get(query) == null) {
            preparedStatementMap.put(query, connection.prepareStatement(query));
        }
    }

    /**
     * Подготовить preparedStatement с GeneratedKeys
     * @param connection Соединение с БД
     * @param query SQL-запрос
     * @throws SQLException
     */
    public void prepareStatementGeneratedKeys(Connection connection, String query) throws SQLException {
        if (preparedStatementMapGK.get(query) == null) {
            preparedStatementMapGK.put(query, connection.prepareStatement(query,
                    PreparedStatement.RETURN_GENERATED_KEYS));
        }
    }

    /**
     * Выполнить обычный запрос в БД
     * @param connection Соединение с БД
     * @param query SQL-запрос в текстовом виде
     * @param handler Обработчик результата
     * @return Результат
     * @throws SQLException
     */
    public <T> T execQuery(Connection connection, String query, ResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();

        return value;
    }

    /**
     * Выполнить preparedStatement-запрос в БД
     * @param args Аргументы для preparedStatement
     * @param handler Обработчик результата
     * @return Результат
     * @throws SQLException
     */
    public <T> T execQuery(String sql, Map<Integer, Object> args, ResultHandler<T> handler) throws SQLException {
        PreparedStatement pstmt = preparedStatementMap.get(sql);
        if (pstmt == null) {
            throw new SQLException(String.format("Statement \"%s\" wasn't prepared", sql));
        }
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            pstmt.setObject(entry.getKey(), entry.getValue());
        }
        ResultSet rs = pstmt.executeQuery();
        T value = handler.handle(rs);
        rs.close();
        return value;
    }

    /**
     * Выполнить preparedStatement-обновление БД без ответа
     * @param args Аргументы для preparedStatement
     * @throws SQLException
     */
    public void execUpdate(String sql, Map<Integer, Object> args) throws SQLException {
        PreparedStatement pstmt = preparedStatementMap.get(sql);
        if (pstmt == null) {
            throw new SQLException(String.format("Statement \"%s\" wasn't prepared", sql));
        }
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            pstmt.setObject(entry.getKey(), entry.getValue());
        }
        pstmt.executeUpdate();
    }

    /**
     * Выполнить preparedStatement-обновление БД с возвратом сгенерированных ключей
     * @param args Аргументы для preparedStatement
     * @param handler Обработчик результата
     * @return Идентификаторы вставленных данных
     * @throws SQLException
     */
    public <T> T execUpdate(String sql, Map<Integer, Object> args, ResultHandler<T> handler) throws SQLException {
        PreparedStatement pstmt = preparedStatementMapGK.get(sql);
        if (pstmt == null) {
            throw new SQLException(String.format("Statement \"%s\" wasn't prepared", sql));
        }
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            pstmt.setObject(entry.getKey(), entry.getValue());
        }
        pstmt.executeUpdate();
        ResultSet rs = pstmt.getGeneratedKeys();
        T value = handler.handle(rs);
        rs.close();
        return value;
    }

    public void close() throws SQLException {
        for (Map.Entry<String, PreparedStatement> pair : preparedStatementMap.entrySet()) {
            pair.getValue().close();
        }
        for (Map.Entry<String, PreparedStatement> pair : preparedStatementMapGK.entrySet()) {
            pair.getValue().close();
        }
    }
}
