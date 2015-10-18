package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.history.Message;
import com.github.kirivasile.technotrack.session.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 18.10.2015.
 */
public class HistoryCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        if (session.getCurrentUserName() == null) {
            System.out.println("Please login before using this command");
        }
        if (args.length == 1) {
            for (Map.Entry<Calendar, Message> pair : session.getHistory().getMessages().entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    System.out.println(pair.getValue());
                }
            }
        } else if (args.length == 2) {
            List<Message> listOfMessages = new ArrayList<>();
            for (Map.Entry<Calendar, Message> pair : session.getHistory().getMessages().entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    listOfMessages.add(pair.getValue());
                }
            }
            int requiredNumMessages = Integer.parseInt(args[1]);
            int currentNumMessages = listOfMessages.size();
            int minNum = requiredNumMessages > currentNumMessages ? currentNumMessages : requiredNumMessages;
            for (int i = listOfMessages.size() - 1, j = 0; j < minNum; --i, ++j) {
                System.out.println(listOfMessages.get(i));
            }
        }
    }

    @Override
    public String toString() {
        return "/history";
    }
}
