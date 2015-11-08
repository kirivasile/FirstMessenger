package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 13.10.2015.
 */
public class LoginCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        AuthorizationService service = session.getAuthorizationService();
        if (args.length == 3) {
            service.authorizeUser(args[1], args[2], session);
        } else {
            writer.writeUTF("Login command: wrong number of arguments");
        }
    }

    public String toString() {
        return "/login";
    }
}
