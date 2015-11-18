package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.*;
import com.github.kirivasile.technotrack.net.server.DataStore;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 18.10.2015.
 */
public class ChatHistoryCommand implements Command {
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
        if (args.length == 2) {
            int chatId = Integer.parseInt(args[1]);
            ChatStore chatStore = session.getDataStore().getChatStore();
            Chat chat = chatStore.getChat(chatId);
            if (chat == null) {
                message = "Chat not found";
                success = AnswerMessage.Value.ERROR;
                writer.write(protocol.encode(new AnswerMessage(message, success)));
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            Map<Integer, ChatMessage> messageMap = chat.getMessageMap();
            for (Map.Entry<Integer, ChatMessage> pair : messageMap.entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    stringBuilder.append(pair.getValue());
                }
            }
            message = stringBuilder.toString();
            success = AnswerMessage.Value.SUCCESS;
        } else {
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));

        /*if (args.length == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            Map<Integer, ChatMessage> messageMap = session.getDataStore().getMessageStore().getMessagesMap();
            for (Map.Entry<Integer, ChatMessage> pair : messageMap.entrySet()) {
                if (pair.getValue().checkAuthor(session.getCurrentUserName())) {
                    stringBuilder.append(pair.getValue());
                }
            }
            //writer.writeUTF(stringBuilder.toString());
            message = stringBuilder.toString();
            success = AnswerMessage.Value.SUCCESS;
        } else if (args.length == 2) {
            List<ChatMessage> listOfChatMessages = new ArrayList<>();
            Map<Integer, ChatMessage> messageMap = session.getDataStore().getMessageStore().getMessagesMap();
            for (Map.Entry<Integer, ChatMessage> pair : messageMap.entrySet()) {
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
        writer.write(protocol.encode(new AnswerMessage(message, success)));*/
    }

    @Override
    public String toString() {
        return "/history";
    }
}
