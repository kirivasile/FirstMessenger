package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 18.10.2015.
 */
public class UserCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (args.length < 2) {
            //writer.writeUTF("User command: wrong number of arguments\n");
            success = AnswerMessage.Value.NUM_ARGS;
        } else {
            AuthorizationService service = session.getAuthorizationService();
            String newNickName = args[1];
            if (service.changeUserNick(session.getCurrentUserId(), newNickName)) {
                //writer.writeUTF("Now the nickname of user " + session.getCurrentUserName() + " is " + newNickName);
                message = "Now the nickname of user " + session.getCurrentUserName() + " is " + newNickName;
                success = AnswerMessage.Value.SUCCESS;
            } else {
                //writer.writeUTF("Please login before using this command");
                success = AnswerMessage.Value.LOGIN;
            }
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    @Override
    public String toString() {
        return "/user";
    }
}
