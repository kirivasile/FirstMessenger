package com.github.kirivasile.technotrack.net.client;

import com.github.kirivasile.technotrack.message.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Программа клиента
 */
public class ClientMain {
    /**
     * Сокет, через который происходит общение
     */
    private Socket socket;

    /**
     * Поток для чтения данных
     */
    private DataInputStream in;

    /**
     * Поток для ввода данных
     */
    private DataOutputStream out;

    /**
     * Нить, отвечающая за получение сообщений от сервера
     */
    private Thread listenThread;

    /**
     * Нить, отвечающая за отправку сообщений на сервер
     */
    private Thread writeThread;

    /**
     * Очередь, для обмена сообщений для отправки между главной нитью и writeThread
     */
    private BlockingQueue<Message> messagesToWrite;

    /**
     * Классы, отвечающие за работу нитей
     */
    private MessageListener listener;
    private MessageWriter writer;

    public ClientMain() {
    }

    public void init() {
        try {
            int port = 9000;
            socket = new Socket("localhost", port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            messagesToWrite = new ArrayBlockingQueue<>(10);
            listener = new MessageListener(in);
            writer = new MessageWriter(out, messagesToWrite);
            listenThread = new Thread(listener);
            writeThread = new Thread(writer);
            listenThread.start();
            writeThread.start();
        } catch (Exception e) {
            System.err.println("Client initialization: exception caught: " + e.toString());
        }
    }

    /**
     * Обработать команду с консоли
     * @throws Exception
     */
    public void handleUserCommands() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while(true) {
            line = reader.readLine();
            Message writeMessage;
            if (line.startsWith("/")) {
                writeMessage = new Message();
                writeMessage.setMessage(line);
                messagesToWrite.put(writeMessage);
                if (line.equals("/exit")) {
                    System.out.println("Closing..");
                    stopThreads();
                    break;
                }
            } else {
                writeMessage = new Message();
                writeMessage.setMessage(line);
                messagesToWrite.put(writeMessage);
            }
        }
    }

    /**
     * Остановить потоки (Ещё работает неточно)
     */
    public void stopThreads() {
        try {
            //listener.stop();
            //writer.stop();
            listenThread.interrupt();
            writeThread.interrupt();
        } catch (Exception e) {
            System.err.println("Failed to stop threads " + e.toString());
        }
    }

    public static void main(String[] args) {
        ClientMain threadedClient = new ClientMain();
        threadedClient.init();
        try {
            threadedClient.handleUserCommands();
        } catch (Exception e) {
            System.err.println("Client: exception caugth: " + e.toString());
        }
    }
}
