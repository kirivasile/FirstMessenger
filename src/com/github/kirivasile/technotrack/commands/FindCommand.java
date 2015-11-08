package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.history.Message;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Kirill on 18.10.2015.
 */
public class FindCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        if (args.length != 2) {
            writer.writeUTF("Wrong number of arguments");
            return;
        }
        if (session.getCurrentUserName() == null) {
            writer.writeUTF("Please login before using this command");
            return;
        }
        StringBuilder messageBuilder = new StringBuilder();
        Map<Calendar, Message> messageMap = session.getHistory().getMessagesMap();
        for (Map.Entry<Calendar, Message> pair : messageMap.entrySet()) {
            if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                String message = pair.getValue().getMessage();
                if (message.matches(args[1])) {
                    messageBuilder.append(String.format("Message: \"%s\", %s\n", message, pair.getValue().getTime()));
                }
            }
        }
        writer.writeUTF(messageBuilder.toString());
    }

    @Override
    public String toString() {
        return "/find";
    }
}
