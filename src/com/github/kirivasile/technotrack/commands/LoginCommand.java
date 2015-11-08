package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.session.Session;

import java.io.BufferedReader;

/**
 * Created by Kirill on 13.10.2015.
 */
public class LoginCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        BufferedReader reader = session.getReader();
        AuthorizationService service = session.getAuthorizationService();
        if (args.length == 1) {
            service.registerUser(session);
        } else if (args.length == 3) {
            service.authorizeUser(args[1], args[2], session);
        } else {
            System.out.println("Login command: wrong number of arguments");
        }
    }

    public String toString() {
        return "/login";
    }
}
