package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 09.11.2015.
 */
public class ChangePasswordCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (args.length == 3) {
            String currentUserName = session.getCurrentUserName();
            if (currentUserName == null) {
                //writer.writeUTF("Please login before using this command");
                success = AnswerMessage.Value.LOGIN;
            } else {
                String oldPassword = args[1];
                String newPassword = args[2];
                AuthorizationService service = session.getAuthorizationService();
                int result = service.changePassword(currentUserName, oldPassword, newPassword);
                if (result == 1) {
                    //writer.writeUTF("Old password isn't correct");
                    message = "Old password isn't correct";
                    success = AnswerMessage.Value.ERROR;
                } else if (result == 0) {
                    //writer.writeUTF("Password was successfully changed");
                    message = "Password was successfully changed";
                    success = AnswerMessage.Value.SUCCESS;
                } else {
                    //writer.writeUTF("Can't find you. Don't worry. It happens");
                    message = "Can't find you. Don't worry. It happens";
                    success = AnswerMessage.Value.ERROR;
                }
            }
        } else {
            //writer.writeUTF("Wrong number of arguments");
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public String toString() {
        return "/user_pass";
    }
}
