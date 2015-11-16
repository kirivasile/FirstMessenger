package com.github.kirivasile.technotrack.message;

import java.io.*;
import java.util.*;

/**
 * Created by Kirill on 18.10.2015.
 */
public class FileMessageStore implements AutoCloseable, MessageStore {
    private Map<Integer, ChatMessage> messages;
    private File historyFile;

    public FileMessageStore() {
        messages = new TreeMap<>();
        try {
            historyFile = new File("message.db");
            if (!historyFile.exists()) {
                if (!historyFile.createNewFile()) {
                    System.err.println("Unable to create message file");
                    return;
                }
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile.getAbsolutePath()))) {
                readDataFromFile(reader);
            } catch (Exception e) {
                System.err.println("Error in reading from file: " + e.toString());
            }
        } catch (IOException e) {
            System.err.println("Error in opening message file: " + e.toString());
        }
    }

    public synchronized void addMessage(String from, String message) {
        Calendar date = new GregorianCalendar();
        int id = messages.size();
        ChatMessage input = new ChatMessage();
        input.setFrom(from);
        input.setMessage(message);
        input.setDate(date);
        input.setId(id);
        messages.put(id, input);
    }

    public synchronized Map<Integer, ChatMessage> getMessagesMap() {
        return messages;
    }

    @Override
    public synchronized void close() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile.getAbsolutePath()))) {
            writeDataToFile(writer);
        } catch (Exception e) {
            System.err.println("Error in writing to file: " + e.toString());
        }
    }

    private synchronized void readDataFromFile(BufferedReader reader) throws Exception {
        while (true) {
            String idString = reader.readLine();
            if (idString == null) {
                break;
            }
            int id = Integer.parseInt(idString);
            String from = reader.readLine();
            String message = reader.readLine();
            String date = reader.readLine();
            if (from == null || message == null || date == null) {
                System.err.println("Incorrect message data");
                return;
            }
            String[] parsedDate = date.split(":");
            int day =Integer.parseInt(parsedDate[0]);
            int month = Integer.parseInt(parsedDate[1]);
            int year = Integer.parseInt(parsedDate[2]);
            int hour = Integer.parseInt(parsedDate[3]);
            int minute = Integer.parseInt(parsedDate[4]);
            int second = Integer.parseInt(parsedDate[5]);
            Calendar calendarDate = new GregorianCalendar(year, month, day, hour, minute, second);
            ChatMessage input = new ChatMessage();
            input.setFrom(from);
            input.setMessage(message);
            input.setDate(calendarDate);
            input.setId(id);
            messages.put(id, input);
        }
    }

    private synchronized void writeDataToFile(BufferedWriter writer) throws Exception {
        for (Map.Entry<Integer, ChatMessage> pair : messages.entrySet()) {
            writer.write(pair.getValue().toFileString() + "\n");
        }
    }
}
