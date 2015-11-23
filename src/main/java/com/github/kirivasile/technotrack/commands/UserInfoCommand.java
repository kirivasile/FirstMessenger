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
            int currentUserId = session.getCurrentUserId();
            if (currentUserId == -1) {
                success = AnswerMessage.Value.NUM_ARGS;
            } else {
                AuthorizationService service = session.getAuthorizationService();
                User user = service.getUserInfo(currentUserId);
                if (user == null) {
                    message = "Can't find you. Don't worry.";
                    success = AnswerMessage.Value.ERROR;
                } else {
                    message = String.format("Username: %s," +
                                                " nickname: %s, Id: %d", user.getName(), user.getNick(), user.getId());
                    success = AnswerMessage.Value.SUCCESS;
                }
            }
        } else if (args.length == 2) {
            AuthorizationService service = session.getAuthorizationService();
            User user = service.getUserInfo(Integer.parseInt(args[1]));
            if (user == null) {
                message = "Can't find user: " + args[1];
                success = AnswerMessage.Value.ERROR;
            } else {
                message = String.format("Username: %s," +
                        " nickname: %s, Id: %d", user.getName(), user.getNick(), user.getId());
                success = AnswerMessage.Value.SUCCESS;
            }
        } else {
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public String toString() {
        return "/user_info";
    }
}
