package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Chat;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.net.server.DataStore;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class ChatListCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        AnswerMessage.Value success;
        String message = "";
        DataStore dataStore = session.getDataStore();
        if (session.getCurrentUserId() == -1) {
            success = AnswerMessage.Value.LOGIN;
        } else {
            success = AnswerMessage.Value.SUCCESS;
            Map<Integer, Chat> result = dataStore.getChatStore().getChatList();
            StringBuilder builder = new StringBuilder();
            builder.append("Chat list:\n");
            for (Map.Entry<Integer, Chat> it : result.entrySet()) {
                builder.append("Chat #");
                builder.append(it.getKey());
                builder.append(", users: ");
                List<Integer> participantsId = it.getValue().getParticipantIds();
                for (Integer userIdIt : participantsId) {
                    builder.append(userIdIt);
                    builder.append(", ");
                }
                builder.append("\n");
            }
            message = builder.toString();
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public String toString() {
        return "/chat_list";
    }
}
