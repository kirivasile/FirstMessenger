package com.github.kirivasile.technotrack.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
     * @param connection Соединение с БД
     * @param query SQL-запрос в текстовом виде
     * @param args Аргументы для preparedStatement
     * @param handler Обработчик результата
     * @return Результат
     * @throws SQLException
     */
    public <T> T execQuery(Connection connection, String query, Map<Integer, Object> args, ResultHandler<T> handler) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            stmt.setObject(entry.getKey(), entry.getValue());
        }
        ResultSet rs = stmt.executeQuery();
        T value = handler.handle(rs);
        rs.close();
        stmt.close();
        return value;
    }

    /**
     * Выполнить preparedStatement-обновление БД без ответа
     * @param connection Соединение с БД
     * @param query SQL-запрос в текстовом виде
     * @param args Аргументы для preparedStatement
     * @throws SQLException
     */
    public void execUpdate(Connection connection, String query, Map<Integer, Object> args) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            stmt.setObject(entry.getKey(), entry.getValue());
        }
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Выполнить preparedStatement-обновление БД с возвратом сгенерированных ключей
     * @param connection Соединение с БД
     * @param query SQL-запрос в текстовом виде
     * @param args Аргументы для preparedStatement
     * @param handler Обработчик результата
     * @return Идентификаторы вставленных данных
     * @throws SQLException
     */
    public <T> T execUpdate(Connection connection, String query, Map<Integer, Object> args, ResultHandler<T> handler) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            stmt.setObject(entry.getKey(), entry.getValue());
        }
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        T value = handler.handle(rs);
        rs.close();
        stmt.close();
        return value;
    }
}
