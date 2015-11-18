package com.github.kirivasile.technotrack.message;

import java.util.Calendar;

/**
 * Created by Kirill on 18.10.2015.
 */
public class ChatMessage extends Message {
    private Calendar date;

    public ChatMessage() {
        this.type = MessageType.TEXT;
    }

    public boolean checkAuthor(String from) {
        return this.authorName.equals(from);
    }

    public String toFileString() {
        /*Format
        * Id
        * AuthorId
        * AuthorName
        * Message
        * dd::mm::yy::hh::mm::ss
        * */
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String dayMonthYear = String.format("%d:%d:%d", day, month, year);
        String time = date.get(Calendar.HOUR_OF_DAY) + ":" +
                      date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return String.format("%d\n%d\n%s\n%s\n%s:%s", id, authorId, authorName, message, dayMonthYear, time);
    }

    public String toString() {
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String dayMonthYear = String.format("%d:%d:%d", day, month, year);
        String time = date.get(Calendar.HOUR_OF_DAY) + ":" +
                      date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return String.format("Id: %d\nFrom: %s\nmessage: \"%s\"\n" +
                "date: %s, time: %s\n", id, authorName, message, dayMonthYear, time);
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String dayMonthYear = String.format("%d:%d:%d", day, month, year);
        String time = date.get(Calendar.HOUR_OF_DAY) + ":" +
                date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
        return String.format("Date: %s, time: %s", dayMonthYear, time);
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
