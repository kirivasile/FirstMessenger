package com.github.kirivasile.technotrack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class MyJdbc {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection c = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/kirivasile", "senthil", "ubuntu");
        Statement stmt;
        String sql;

       /* stmt = c.createStatement();
        sql = "CREATE TABLE COMPANY " +
                "(ID INT PRIMARY KEY     NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " AGE            INT     NOT NULL, " +
                " ADDRESS        CHAR(50), " +
                " SALARY         REAL)";
        sql = "CREATE TABLE CHAT " +
                "(ID INT PRIMARY    KEY     NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();*/

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
                " AUTHOR_ID         TEXT    NOT NULL," +
                " VALUE             TEXT    NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();

        stmt = c.createStatement();
        sql = "CREATE TABLE IF NOT EXISTS userschat " +
                "(ID SERIAL PRIMARY KEY," +
                " USER_ID           INT     NOT NULL," +
                " CHAT_ID           INT     NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();

        stmt = c.createStatement();
        sql = "CREATE TABLE IF NOT EXISTS chatmessage " +
                "(ID SERIAL PRIMARY KEY," +
                " MESSAGE_ID        INT     NOT NULL," +
                " CHAT_ID           INT     NOT NULL)";
        stmt.executeUpdate(sql);
        stmt.close();
        c.close();



        /*stmt = c.createStatement();
        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (1, 'Paul', 32, 'California', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (2, 'Allen', 25, 'Texas', 15000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (3, 'Teddy', 23, 'Norway', 20000.00 );";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO COMPANY (ID,NAME,AGE,ADDRESS,SALARY) "
                + "VALUES (4, 'Mark', 25, 'Rich-Mond ', 65000.00 );";
        stmt.executeUpdate(sql);
        stmt.close();
        c.commit();


        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY;");
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String address = rs.getString("address");
            float salary = rs.getFloat("salary");
            System.out.println("ID = " + id);
            System.out.println("NAME = " + name);
            System.out.println("AGE = " + age);
            System.out.println("ADDRESS = " + address);
            System.out.println("SALARY = " + salary);
            System.out.println();
        }
        rs.close();
        stmt.close();
        c.close();*/
    }
}
