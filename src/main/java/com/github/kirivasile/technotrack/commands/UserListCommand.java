package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.User;
import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.net.server.DataStore;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.List;

/**
 * Created by Kirill on 18.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class UserListCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        AnswerMessage.Value success = AnswerMessage.Value.SUCCESS;
        DataStore dataStore = session.getDataStore();
        List<User> userList = dataStore.getUserStore().getUserList();
        StringBuilder builder = new StringBuilder();
        builder.append("User List:\n");
        for (User it : userList) {
            builder.append(String.format("Id: %d, Username: %s, Nick: %s\n", it.getId(), it.getName(), it.getNick()));
        }
        String message = builder.toString();
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public String toString() {
        return "/user_list";
    }
}
