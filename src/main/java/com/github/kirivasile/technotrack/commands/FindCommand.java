package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.ChatMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.Map;

/**
 * Created by Kirill on 18.10.2015.
 */
public class FindCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (args.length != 2) {
            //writer.writeUTF("Wrong number of arguments");
            success = AnswerMessage.Value.NUM_ARGS;
        } else {
            if (session.getCurrentUserName() == null) {
                //writer.writeUTF("Please login before using this command");
                success = AnswerMessage.Value.LOGIN;
            } else {
                StringBuilder messageBuilder = new StringBuilder();
                Map<Integer, ChatMessage> messageMap = session.getFileMessageStore().getMessagesMap();
                for (Map.Entry<Integer, ChatMessage> pair : messageMap.entrySet()) {
                    if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                        String msg = pair.getValue().getMessage();
                        if (msg.matches(args[1])) {
                            messageBuilder.append(String.format("Message: \"%s\", %s\n", msg, pair.getValue().getTime()));
                        }
                    }
                }
                //writer.writeUTF(messageBuilder.toString());
                message = messageBuilder.toString();
                success = AnswerMessage.Value.SUCCESS;
            }
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    @Override
    public String toString() {
        return "/find";
    }
}
