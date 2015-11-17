package com.github.kirivasile.technotrack.net.client;

import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Message;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;

import java.io.DataInputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class MessageListener implements Runnable {
    private DataInputStream reader;
    private Protocol<AnswerMessage> readProtocol;

    public MessageListener(DataInputStream reader) {
        this.reader = reader;
        readProtocol = new SerializationProtocol<>();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                byte[] buf = new byte[1024 * 60];
                int readSize = reader.read(buf);
                if (readSize > 0) {
                    AnswerMessage answer = readProtocol.decode(buf);
                    handleAnswer(answer);
                }
            }
        } catch (Exception e) {
            System.err.println("Listener: exception cauht: " + e.toString());
        }
    }

    public void handleAnswer(AnswerMessage answer) throws Exception {
        AnswerMessage.Value type = answer.getResult();
        String message = answer.getMessage();
        switch (type) {
            case SUCCESS:
                if (!message.equals("Message delivered")) {
                    System.out.println(message);
                }
                break;
            case ERROR:
                System.out.println("Error:");
                System.out.println(message);
                break;
            case LOGIN:
                System.out.println("Error:\nPlease login first");
                break;
            case NUM_ARGS:
                System.out.println("Error:\nWrong number of arguments");
                break;
            case CHAT:
                String from = answer.getFrom();
                int id = answer.getId();
                System.out.println(String.format("%s(id:%d): %s", from, id, message));
                break;
        }
    }

    public void stop() throws Exception {
        Thread.currentThread().interrupt();
    }
}
