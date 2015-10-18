package com.github.kirivasile.technotrack.history;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Kirill on 18.10.2015.
 */
public class Message {
    private String from;
    private String message;
    private Calendar date;

    public Message(String from, String message, Calendar date) {
        this.from = from;
        this.message = message;
        this.date = date;
    }

    public boolean checkAuthor(String from) {
        return this.from.equals(from);
    }

    public String toFileString() {
        /*Format
        * From
        * Message
        * dd::mm::yy::hh::mm::ss
        * */
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String dayMonthYear = String.format("%d:%d:%d", day, month, year);
        String time = date.get(Calendar.HOUR_OF_DAY) + ":" +
                      date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return String.format("%s\n%s\n%s:%s", from, message, dayMonthYear, time);
    }

    public String toString() {
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String dayMonthYear = String.format("%d:%d:%d", day, month, year);
        String time = date.get(Calendar.HOUR_OF_DAY) + ":" +
                      date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return String.format("From: %s\nmessage: \"%s\"\ndate: %s, time: %s\n", from, message, dayMonthYear, time);

    }
}
