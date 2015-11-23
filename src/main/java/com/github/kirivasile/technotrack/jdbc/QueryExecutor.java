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

public class QueryExecutor {
    //Usual statement
    public <T> T execQuery(Connection connection, String query, ResultHandler<T> handler) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet result = stmt.getResultSet();
        T value = handler.handle(result);
        result.close();
        stmt.close();

        return value;
    }

    //PreparedStatement
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

    //PreparedStatement for update
    public void execUpdate(Connection connection, String query, Map<Integer, Object> args) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        for (Map.Entry<Integer, Object> entry : args.entrySet()) {
            stmt.setObject(entry.getKey(), entry.getValue());
        }
        stmt.executeUpdate();
        stmt.close();
    }

    //PreparedStatement for update with result
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
