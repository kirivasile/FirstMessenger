package com.github.kirivasile.technotrack.history;

import java.io.*;
import java.util.*;

/**
 * Created by Kirill on 18.10.2015.
 */
public class History {
    private Map<Calendar, Message> messages;
    private File historyFile;

    public History() {
        messages = new TreeMap<>();
        try {
            historyFile = new File("history.db");
            if (!historyFile.exists()) {
                if (!historyFile.createNewFile()) {
                    System.err.println("Unable to create history file");
                    return;
                }
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(historyFile.getAbsolutePath()))) {
                String line;
                while (true) {
                    String from = reader.readLine();
                    if (from == null) {
                        break;
                    }
                    String message = reader.readLine();
                    String date = reader.readLine();
                    if (message == null || date == null) {
                        System.err.println("Incorrect history data");
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
                    messages.put(calendarDate, new Message(from, message, calendarDate));
                }
            } catch (Exception e) {
                System.err.println("Error in reading from file: " + e.toString());
            }
        } catch (IOException e) {
            System.err.println("Error in opening history file: " + e.toString());
        }
    }

    public void addMessage(String from, String message) {
        Calendar date = new GregorianCalendar();
        Message input = new Message(from, message, date);
        messages.put(date, input);
    }

    public void close() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(historyFile.getAbsolutePath()))) {
            for (Map.Entry<Calendar, Message> pair : messages.entrySet()) {
                writer.write(pair.getValue().toFileString() + "\n");
            }
        } catch (Exception e) {
            System.err.println("Error in writing to file: " + e.toString());
        }
    }

    public Map<Calendar, Message> getMessagesMap() {
        return messages;
    }
}
