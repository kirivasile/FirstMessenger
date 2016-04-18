package com.github.kirivasile.technotrack.net.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Kirill on 8.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Программа сервера
 */
public class ServerMain {
    public static void main(String[] args) {
        try {
            MultiThreadServer server = new MultiThreadServer(9000);
            Thread serverThread = new Thread(server);
            serverThread.start();
            System.out.println("Server started, type /stop to stop it");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                String line = reader.readLine();
                if (line.equals("/stop")) {
                    server.close();
                    break;
                }
            }
            serverThread.join();
        } catch (Exception e) {
            System.err.println("ServerMain: exception caught: " + e.toString());
        }
    }
}
