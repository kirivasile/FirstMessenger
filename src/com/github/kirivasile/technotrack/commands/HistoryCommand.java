package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.history.Message;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
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
        DataOutputStream writer = session.getWriter();
        if (session.getCurrentUserName() == null) {
            writer.writeUTF("Please login before using this command");
            return;
        }
        if (args.length == 1) {
            StringBuilder message = new StringBuilder();
            for (Map.Entry<Calendar, Message> pair : session.getMessageStore().getMessagesMap().entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    message.append(pair.getValue());
                }
            }
            writer.writeUTF(message.toString());
        } else if (args.length == 2) {
            List<Message> listOfMessages = new ArrayList<>();
            for (Map.Entry<Calendar, Message> pair : session.getMessageStore().getMessagesMap().entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    listOfMessages.add(pair.getValue());
                }
            }
            int requiredNumMessages = Integer.parseInt(args[1]);
            int currentNumMessages = listOfMessages.size();
            int minNum = requiredNumMessages > currentNumMessages ? currentNumMessages : requiredNumMessages;
            StringBuilder message = new StringBuilder();
            for (int i = listOfMessages.size() - 1, j = 0; j < minNum; --i, ++j) {
                message.append(listOfMessages.get(i));
            }
            writer.writeUTF(message.toString());
        } else {
            writer.writeUTF("Wrong number of arguments");
        }
    }

    @Override
    public String toString() {
        return "/history";
    }
}
