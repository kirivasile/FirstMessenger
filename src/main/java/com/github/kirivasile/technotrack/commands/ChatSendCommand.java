package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.*;
import com.github.kirivasile.technotrack.net.server.DataStore;
import com.github.kirivasile.technotrack.session.Session;
import com.github.kirivasile.technotrack.session.SessionManager;

import java.io.DataOutputStream;
import java.util.List;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class ChatSendCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        DataStore dataStore = session.getDataStore();
        int authorId = session.getCurrentUserId();
        String authorName = session.getCurrentUserName();
        if (authorId == -1) {
            success = AnswerMessage.Value.LOGIN;
            writer.write(protocol.encode(new AnswerMessage(message, success)));
        } else {
            if (args.length < 3) {
                success = AnswerMessage.Value.NUM_ARGS;
                writer.write(protocol.encode(new AnswerMessage(message, success)));
                return;
            }
            int chatId = Integer.parseInt(args[1]);
            StringBuilder builder = new StringBuilder();
            for (int i = 2; i < args.length; ++i) {
                builder.append(args[i] + " ");
            }
            String messageFromUser = builder.toString();
            ChatStore chatStore = dataStore.getChatStore();
            Chat chat = chatStore.getChat(chatId);
            if (chat == null) {
                message = "Chat not found";
                success = AnswerMessage.Value.ERROR;
                writer.write(protocol.encode(new AnswerMessage(message, success)));
                return;
            }
            List<Integer> participants = chat.getParticipantIds();
            if (!participants.contains(authorId)) {
                message = "You are not allowed to write in this chat";
                success = AnswerMessage.Value.ERROR;
                writer.write(protocol.encode(new AnswerMessage(message, success)));
                return;
            }
            SessionManager manager = session.getSessionManager();
            for (Integer id : participants) {
                Session destinationSession = manager.getSessionById(id);
                success = AnswerMessage.Value.CHAT;
                AnswerMessage answer = new AnswerMessage(messageFromUser, success);
                answer.setFrom(String.format("Chat #%d, from: %s", chatId, authorName));
                answer.setId(authorId);
                DataOutputStream destinationWriter = destinationSession.getWriter();
                destinationWriter.write(protocol.encode(answer));
            }
        }
    }

    public String toString() {
        return "/chat_send";
    }
}
