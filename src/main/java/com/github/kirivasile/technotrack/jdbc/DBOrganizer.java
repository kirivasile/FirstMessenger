package com.github.kirivasile.technotrack.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Организация базы данных перед запуском(Создание таблиц).
 */
public class DBOrganizer {
    public static void reorganizeDB(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/kirivasile", "senthil", "ubuntu");
        Statement stmt;
        String sql;

        stmt = c.createStatement();
        sql = "CREATE TABLE IF NOT EXISTS users " +
                "(ID SERIAL PRIMARY KEY," +
                " LOGIN             TEXT    NOT NULL," +
                " PASSWORD          TEXT    NOT NULL," +
                " NICK              TEXT    NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();

        stmt = c.createStatement();
        sql = "CREATE TABLE IF NOT EXISTS chats " +
                "(ID SERIAL PRIMARY KEY," +
                " TEMP TEXT NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();

        stmt = c.createStatement();
        sql = "CREATE TABLE IF NOT EXISTS message " +
                "(ID SERIAL PRIMARY KEY," +
                " AUTHOR_ID         INT    NOT NULL," +
                " VALUE             TEXT    NOT NULL," +
                " CHAT_ID           INT     NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();

        stmt = c.createStatement();
        sql = "CREATE TABLE IF NOT EXISTS userschat " +
                "(ID SERIAL PRIMARY KEY," +
                " USER_ID           INT     NOT NULL," +
                " CHAT_ID           INT     NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();

        c.close();
    }
}
