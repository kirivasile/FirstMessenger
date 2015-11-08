package com.github.kirivasile.technotrack.net.client;

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
            String line = null;
            while(true) {
                System.out.print("$ ");
                line = reader.readLine();
                if (line.equals("/exit")) {
                    System.out.println("Closing...");
                    out.writeUTF(line);
                    out.flush();
                    break;
                }
                out.writeUTF(line);
                out.flush();
                String recieved = in.readUTF();
                if (!recieved.equals("Message delivered")) {
                    System.out.println("Received: " + recieved);
                }
            }
        } catch (Exception e) {
            System.err.println("Client: exception caught: " + e.toString());
        }
    }
}
