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
        if (session.getCurrentUserName() == null) {
            success = AnswerMessage.Value.LOGIN;
            writer.write(protocol.encode(new AnswerMessage(message, success)));
            return;
        }
        if (args.length >= 3) {
            int chatId = Integer.parseInt(args[1]);
            ChatStore chatStore = session.getDataStore().getChatStore();
            Chat chat = chatStore.getChat(chatId);
            if (chat == null) {
                message = "Chat not found";
                success = AnswerMessage.Value.ERROR;
                writer.write(protocol.encode(new AnswerMessage(message, success)));
                return;
            }
            StringBuilder regexBuilder = new StringBuilder();
            for (int i = 2; i < args.length; ++i) {
                regexBuilder.append(args[i]);
                if (i != args.length - 1) {
                    regexBuilder.append(' ');
                }
            }
            String regex = regexBuilder.toString();
            StringBuilder stringBuilder = new StringBuilder();
            Map<Integer, Message> messageMap = chat.getMessageMap();
            for (Map.Entry<Integer, Message> pair : messageMap.entrySet()) {
                String messageValue = pair.getValue().getMessage();
                if (messageValue.matches(regex)) {
                    stringBuilder.append(pair.getValue().toString());
                    stringBuilder.append("\n");
                }
            }
            message = stringBuilder.toString();
            success = AnswerMessage.Value.SUCCESS;
        } else {
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    @Override
    public String toString() {
        return "/chat_find";
    }
}
