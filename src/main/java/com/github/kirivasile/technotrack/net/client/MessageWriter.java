package com.github.kirivasile.technotrack.net.client;

import com.github.kirivasile.technotrack.message.Message;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;

import java.io.DataOutputStream;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Класс, отправляющий сообщения серверу
 */
public class MessageWriter implements Runnable {
    /**
     * @see ClientMain#out
     */
    private DataOutputStream out;

    /**
     * @see ClientMain#messagesToWrite
     */
    private BlockingQueue<Message> messagesToWrite;

    /**
     * Протокол кодирования
     */
    private Protocol<Message> writeProtocol;

    public MessageWriter(DataOutputStream out, BlockingQueue<Message> messagesToWrite) {
        this.out = out;
        this.messagesToWrite = messagesToWrite;
        writeProtocol = new SerializationProtocol<>();
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message messageToWrite = messagesToWrite.take();
                out.write(writeProtocol.encode(messageToWrite));
                out.flush();
            }
        } catch (Exception e) {
            System.err.println("MessageWriter: exception caught " + e.toString());
        }
    }
}
