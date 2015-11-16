package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.authorization.User;
import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 09.11.2015.
 */
public class UserInfoCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (args.length == 1) {
            String currentUserName = session.getCurrentUserName();
            if (currentUserName == null) {
                //writer.writeUTF("Please login before using this command");
                success = AnswerMessage.Value.NUM_ARGS;
            } else {
                AuthorizationService service = session.getAuthorizationService();
                User user = service.getUserInfo(currentUserName);
                if (user == null) {
                    //writer.writeUTF("Can't find you. Don't worry.");
                    message = "Can't find you. Don't worry.";
                    success = AnswerMessage.Value.ERROR;
                } else {
                    message = String.format("Username: %s," +
                                                " nickname: %s", user.getName(), user.getNick());
                    success = AnswerMessage.Value.SUCCESS;
                    //writer.writeUTF(data);
                }
            }
        } else if (args.length == 2) {
            AuthorizationService service = session.getAuthorizationService();
            User user = service.getUserInfo(args[1]);
            if (user == null) {
                //writer.writeUTF("Can't find user: " + args[1]);
                message = "Can't find user: " + args[1];
                success = AnswerMessage.Value.ERROR;
            } else {
                message = String.format("Username: %s," +
                        " nickname: %s", user.getName(), user.getNick());
                //writer.writeUTF(data);
                success = AnswerMessage.Value.SUCCESS;
            }
        } else {
            //writer.writeUTF("Wrong number of arguments");
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public String toString() {
        return "/user_info";
    }
}
