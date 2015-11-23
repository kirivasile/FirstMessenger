package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.DBUserStore;
import com.github.kirivasile.technotrack.jdbc.DBOrganizer;
import com.github.kirivasile.technotrack.message.DBChatStore;
import com.github.kirivasile.technotrack.session.SessionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill on 08.11.2015.
 */
public class MultiThreadServer implements Runnable, AutoCloseable {
    private int serverPort;
    private ServerSocket serverSocket;
    private boolean isStopped;
    private List<Thread> clientThreads;
    private DataStore dataStore;
    private SessionManager sessionManager;
    private Connection connection;

    public MultiThreadServer(int serverPort) {
        try {
            this.serverPort = serverPort;
            isStopped = false;
            clientThreads = new ArrayList<>();
            DBOrganizer.reorganizeDB(null);
            connection = DriverManager.getConnection("jdbc:postgresql://178.62.140.149:5432/kirivasile",
                    "senthil", "ubuntu");
            dataStore = new DataStore(new DBUserStore(connection), new DBChatStore(connection), connection);
            sessionManager = new SessionManager();
        } catch (Exception e) {
            System.err.println("Server: exception caught " + e.toString());
        }
    }


    /**
     *
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            while (!isStopped()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                Thread thread = new Thread(new CommandHandler(in, out, dataStore, sessionManager));
                        // TODO: лучше CommandHandler сделать Runnable в вашем случае
                        // и передавать его в new Thread() - будет читаемее
                clientThreads.add(thread);
                thread.start();
            }
        } catch (SocketException e) {
            //Server was closed
            if (!e.getMessage().equals("socket closed")) {
                System.err.println("Server: exception caught: " + e.toString());
            }
        } catch (Exception e) {
            System.err.println("Server: exception caught: " + e.toString());
        } finally {
            close();
        }
    }

    public synchronized boolean isStopped() {
        return isStopped;
    }

    private synchronized void stop() {
        isStopped = true;
    }

    @Override
    public void close() {
        stop();
        try {
            serverSocket.close();
            dataStore.close();
            connection.close();
            //for (Thread it : clientThreads) {
                // FIXME: потоки никогда не остановятся, потому что для них нет никакого механизма останова
                //it.join();
                //По поводу остановки я подходил к Дмитрию на семинаре. Мы пока не смогли сделать так, чтобы
                //потоки останавливались без задержки
            //}
        } catch (Exception e) {
            System.err.println("Server: error in closing: " + e.toString());
        }
    }
}
