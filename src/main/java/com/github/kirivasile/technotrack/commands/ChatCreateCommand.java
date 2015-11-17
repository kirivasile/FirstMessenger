package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.net.server.DataStore;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class ChatCreateCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        DataStore dataStore = session.getDataStore();
        if (session.getCurrentUserId() == -1) {
            success = AnswerMessage.Value.LOGIN;
        } else {
            if (args.length != 2) {
                success = AnswerMessage.Value.NUM_ARGS;
            } else {
                UserStore userStore = dataStore.getUserStore();
                List<Integer> participants = new ArrayList<>();
                String[] parsedArg = args[1].split(",");
                for (String it : parsedArg) {
                    int id = Integer.parseInt(it);
                    participants.add(id);
                    if (userStore.getUser(id) == null) {
                        message = String.format("There is no user with id %d", id);
                        success = AnswerMessage.Value.ERROR;
                        writer.write(protocol.encode(new AnswerMessage(message, success)));
                        return;
                    }
                }
                if (participants.size() == 1) {
                    int id1 = session.getCurrentUserId();
                    int id2 = participants.get(0);
                    int result = dataStore.getChatStore().createPrivateChat(id1, id2);
                    if (result != -1) {
                        message = "Chat was successfully created";
                    } else {
                        message = "Chat is existing";
                    }
                } else {
                    dataStore.getChatStore().createChat(participants);
                    message = "Chat was successfully created";
                }
                success = AnswerMessage.Value.SUCCESS;
            }
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public String toString() {
        return "/chat_create";
    }
}
