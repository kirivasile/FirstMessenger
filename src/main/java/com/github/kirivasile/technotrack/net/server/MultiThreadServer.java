package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.FileUserStore;
import com.github.kirivasile.technotrack.message.FileChatStore;
import com.github.kirivasile.technotrack.message.FileMessageStore;
import com.github.kirivasile.technotrack.session.SessionManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
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

    public MultiThreadServer(int serverPort) {
        this.serverPort = serverPort;
        isStopped = false;
        clientThreads = new ArrayList<>();
        dataStore = new DataStore(new FileUserStore(), new FileMessageStore(), new FileChatStore());
        sessionManager = new SessionManager();
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
                Thread thread = new Thread(() -> {
                    try {
                        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                        // TODO: лучше CommandHandler сделать Runnable в вашем случае
                        // и передавать его в new Thread() - будет читаемее
                        CommandHandler commandHandler = new CommandHandler(in, out, dataStore, sessionManager);
                        commandHandler.handle();
                    } catch (IOException e) {
                        System.err.println("ServerWork: Error in client thread of server " + e.toString());
                    }
                });
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
            for (Thread it : clientThreads) {
                // FIXME: потоки никогда не остановятся, потому что для них нет никакого механизма останова
                it.join();
            }
        } catch (Exception e) {
            System.err.println("Server: error in closing: " + e.toString());
        }
    }
}
