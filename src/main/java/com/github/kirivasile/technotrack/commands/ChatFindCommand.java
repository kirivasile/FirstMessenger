package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.*;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.Map;

/**
 * Created by Kirill on 18.10.2015.
 */
public class ChatFindCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (args.length != 2) {
            success = AnswerMessage.Value.NUM_ARGS;
        } else {
            if (session.getCurrentUserName() == null) {
                success = AnswerMessage.Value.LOGIN;
            } else {
                int chatId = Integer.parseInt(args[1]);
                ChatStore chatStore = session.getDataStore().getChatStore();
                Chat chat = chatStore.getChat(chatId);
                if (chat == null) {
                    message = "Chat not found";
                    success = AnswerMessage.Value.ERROR;
                    writer.write(protocol.encode(new AnswerMessage(message, success)));
                    return;
                }
                StringBuilder messageBuilder = new StringBuilder();
                Map<Integer, Message> messageMap = chat.getMessageMap();
                for (Map.Entry<Integer, Message> pair : messageMap.entrySet()) {
                    if (pair.getValue().getAuthorId() == session.getCurrentUserId()) {
                        String msg = pair.getValue().getMessage();
                        if (msg.matches(args[1])) {
                            messageBuilder.append(String.format("Message: \"%s\"\n", msg));
                        }
                    }
                }
                message = messageBuilder.toString();
                success = AnswerMessage.Value.SUCCESS;
            }
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    @Override
    public String toString() {
        return "/chat_find";
    }
}
