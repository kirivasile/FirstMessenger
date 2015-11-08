package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.history.MessageStore;

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

    public MultiThreadServer(int serverPort) {
        this.serverPort = serverPort;
        isStopped = false;
        clientThreads = new ArrayList<>();
        dataStore = new DataStore(new UserStore(), new MessageStore());
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(serverPort);
            while (!isStopped()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");
                Runnable serverWork = () -> {
                    try {
                        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                        CommandHandler commandHandler = new CommandHandler(in, out, dataStore);
                        commandHandler.run();
                    } catch (IOException e) {
                        System.err.println("ServerWork: Error in client thread of server " + e.toString());
                    }
                };
                Thread thread = new Thread(serverWork);
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
        }
    }

    public synchronized boolean isStopped() {
        return isStopped;
    }

    private synchronized void stop() {
        isStopped = true;
    }

    @Override
    public void close() throws Exception {
        stop();
        try {
            serverSocket.close();
            for (Thread it : clientThreads) {
                it.join();
            }
        } catch (Exception e) {
            System.err.println("Server: error in closing: " + e.toString());
        }
    }
}
