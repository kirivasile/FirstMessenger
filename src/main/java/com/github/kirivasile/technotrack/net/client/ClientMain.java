package com.github.kirivasile.technotrack.net.client;

import com.github.kirivasile.technotrack.message.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by Kirill on 08.11.2015.
 */
public class ClientMain {
    public static void main(String[] args) {
        int port = 9000;
        try {
            Socket socket = new Socket("localhost", port);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("You can type messages. Type /exit to close connection and exit");
            Protocol<Message> writeProtocol = new SerializationProtocol<>();
            Protocol<AnswerMessage> readProtocol = new SerializationProtocol<>();
            String line = null;
            while(true) {
                System.out.print("$ ");
                line = reader.readLine();
                Message writeMessage;
                if (line.startsWith("/")) {
                    writeMessage = new CommandMessage();
                    writeMessage.setMessage(line);
                    out.write(writeProtocol.encode(writeMessage));
                    out.flush();
                    if (line.equals("/exit")) {
                        System.out.println("Closing..");
                        break;
                    }
                } else {
                    writeMessage = new ChatMessage();
                    writeMessage.setMessage(line);
                    out.write(writeProtocol.encode(writeMessage));
                    out.flush();
                }
                //String recieved = in.readUTF();
                byte[] buf = new byte[1024 * 60];
                in.read(buf);
                AnswerMessage answer = readProtocol.decode(buf);
                handleAnswer(answer);
            }
        } catch (Exception e) {
            System.err.println("Client: exception caught: " + e.toString());
        }
    }

    public static void handleAnswer(AnswerMessage answer) throws Exception {
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
        }
    }
}
