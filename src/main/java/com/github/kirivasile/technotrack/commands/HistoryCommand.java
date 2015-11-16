package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.ChatMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 18.10.2015.
 */
public class HistoryCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (session.getCurrentUserName() == null) {
            //writer.writeUTF("Please login before using this command");
            success = AnswerMessage.Value.LOGIN;
            writer.write(protocol.encode(new AnswerMessage(message, success)));
            return;
        }
        if (args.length == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<Integer, ChatMessage> pair : session.getFileMessageStore().getMessagesMap().entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    stringBuilder.append(pair.getValue());
                }
            }
            //writer.writeUTF(stringBuilder.toString());
            message = stringBuilder.toString();
            success = AnswerMessage.Value.SUCCESS;
        } else if (args.length == 2) {
            List<ChatMessage> listOfChatMessages = new ArrayList<>();
            for (Map.Entry<Integer, ChatMessage> pair : session.getFileMessageStore().getMessagesMap().entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    listOfChatMessages.add(pair.getValue());
                }
            }
            int requiredNumMessages = Integer.parseInt(args[1]);
            int currentNumMessages = listOfChatMessages.size();
            int minNum = requiredNumMessages > currentNumMessages ? currentNumMessages : requiredNumMessages;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = listOfChatMessages.size() - 1, j = 0; j < minNum; --i, ++j) {
                stringBuilder.append(listOfChatMessages.get(i));
            }
            //writer.writeUTF(stringBuilder.toString());
            message = stringBuilder.toString();
            success = AnswerMessage.Value.SUCCESS;
        } else {
            //writer.writeUTF("Wrong number of arguments");
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    @Override
    public String toString() {
        return "/history";
    }
}
