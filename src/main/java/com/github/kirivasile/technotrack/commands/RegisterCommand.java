package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 08.11.2015.
 */
public class RegisterCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        AnswerMessage.Value success;
        if (args.length == 3) {
            AuthorizationService service = session.getAuthorizationService();
            service.registerUser(args[1], args[2], session);
        } else {
            DataOutputStream writer = session.getWriter();
            //writer.writeUTF("Wrong number of arguments");
            success = AnswerMessage.Value.NUM_ARGS;
            writer.write(protocol.encode(new AnswerMessage("", success)));
        }
    }

    public String toString() {
        return "/register";
    }
}
