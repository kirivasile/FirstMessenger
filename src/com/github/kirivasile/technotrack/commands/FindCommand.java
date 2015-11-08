package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.history.Message;
import com.github.kirivasile.technotrack.session.Session;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by Kirill on 18.10.2015.
 */
public class FindCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        if (args.length != 2) {
            System.out.println("Wrong number of arguments");
            return;
        }
        if (session.getCurrentUserName() == null) {
            System.out.println("Please login before using this command");
            return;
        }
        Map<Calendar, Message> messageMap = session.getHistory().getMessagesMap();
        for (Map.Entry<Calendar, Message> pair : messageMap.entrySet()) {
            if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                String message = pair.getValue().getMessage();
                if (message.matches(args[1])) {
                    System.out.println(String.format("Message: \"%s\", %s", message, pair.getValue().getTime()));
                }
            }
        }
    }

    @Override
    public String toString() {
        return "/find";
    }
}
