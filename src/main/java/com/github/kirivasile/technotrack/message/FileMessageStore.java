package com.github.kirivasile.technotrack.message;

import org.postgresql.ds.PGPoolingDataSource;

import java.io.*;
import java.util.*;

/**
 * Created by Kirill on 18.10.2015.
 */
public class FileMessageStore implements AutoCloseable, MessageStore {
    private Map<Integer, ChatMessage> messages;
    private File historyFile;
    private File historyDirectory;
    private File chatDataFile;

    public FileMessageStore(String filename, String chatData) {
        /*messages = new TreeMap<>();
        try {
            historyDirectory = new File("messages");
            if (!historyDirectory.exists() || !historyDirectory.isDirectory()) {
                if (!historyDirectory.mkdir()) {
                    System.err.println("Unable to create messages directory");
                }
            }
            chatDataFile = new File("messages" + File.separator + "data" + filename);
            if (!chatDataFile.exists()) {
                if (!chatDataFile.createNewFile()) {
                    System.err.println("Unable to create chat data file");
                    return;
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile.getAbsolutePath()))) {
                writer.write(chatData);
            } catch (Exception e) {
                System.err.println("FileMessageStore: Error in writing to file: " + e.toString());
            }
            historyFile = new File("messages" + File.separator + filename);
            if (!historyFile.exists()) {
                if (!historyFile.createNewFile()) {
                    System.err.println("Unable to create message file");
                    return;
                }
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile.getAbsolutePath()))) {
                readDataFromFile(reader);
            } catch (Exception e) {
                System.err.println("FileMessageStore: Error in reading from file: " + e.toString());
            }
        } catch (IOException e) {
            System.err.println("FileMessageStore: Error in opening message file: " + e.toString());
        }*/
    }

    public synchronized void addMessage(int authorId, String authorName, String message) {
        Calendar date = new GregorianCalendar();
        int id = messages.size();
        ChatMessage input = new ChatMessage();
        input.setAuthorName(authorName);
        input.setMessage(message);
        input.setDate(date);
        input.setId(id);
        input.setAuthorId(authorId);
        messages.put(id, input);
    }

    public synchronized Map<Integer, ChatMessage> getMessagesMap() {
        return messages;
    }

    @Override
    public synchronized void close() {
        /*try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile.getAbsolutePath()))) {
            writeDataToFile(writer);
        } catch (Exception e) {
            System.err.println("FileMessageStore: Error in writing to file: " + e.toString());
        }*/
    }

    private synchronized void readDataFromFile(BufferedReader reader) throws Exception {
        while (true) {
            String idString = reader.readLine();
            if (idString == null) {
                break;
            }
            int id = Integer.parseInt(idString);
            String authorIdString = reader.readLine();
            int authorId = Integer.parseInt(authorIdString);
            String from = reader.readLine();
            String message = reader.readLine();
            String date = reader.readLine();
            if (from == null || message == null || date == null) {
                System.err.println("FileMessageStore: Incorrect message data");
                return;
            }
            String[] parsedDate = date.split(":");
            int day = Integer.parseInt(parsedDate[0]);
            int month = Integer.parseInt(parsedDate[1]);
            int year = Integer.parseInt(parsedDate[2]);
            int hour = Integer.parseInt(parsedDate[3]);
            int minute = Integer.parseInt(parsedDate[4]);
            int second = Integer.parseInt(parsedDate[5]);
            Calendar calendarDate = new GregorianCalendar(year, month, day, hour, minute, second);
            ChatMessage input = new ChatMessage();
            input.setAuthorName(from);
            input.setMessage(message);
            input.setDate(calendarDate);
            input.setId(id);
            input.setAuthorId(authorId);
            messages.put(id, input);
        }
    }

    private synchronized void writeDataToFile(BufferedWriter writer) throws Exception {
        for (Map.Entry<Integer, ChatMessage> pair : messages.entrySet()) {
            writer.write(pair.getValue().toFileString() + "\n");
        }
    }
}
