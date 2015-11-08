package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 18.10.2015.
 */
public class UserCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        if (args.length < 2) {
            writer.writeUTF("User command: wrong number of arguments\n");
            return;
        }
        AuthorizationService service = session.getAuthorizationService();
        String newNickName = args[1];
        if (service.changeUserNick(session.getCurrentUserName(), newNickName)) {
            writer.writeUTF("Now the nickname of user " + session.getCurrentUserName() + " is " + newNickName);
        } else {
            writer.writeUTF("Please login before using this command");
        }
    }

    @Override
    public String toString() {
        return "/user";
    }
}
