package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.AuthorizationService;
import com.github.kirivasile.technotrack.UserStore;

import java.io.BufferedReader;

/**
 * Created by Kirill on 13.10.2015.
 */
public class LoginCommand implements Command {
    @Override
    public void run(String[] args, BufferedReader reader) throws Exception {
        UserStore userStore = new UserStore();
        AuthorizationService service = new AuthorizationService(userStore);
        if (args.length == 1) {
            service.registerUser(reader);
        } else if (args.length == 3) {
            service.authorizeUser(args[1], args[2]);
        } else {
            System.out.println("Login command: wrong number of arguments");
        }
    }

    public String toString() {
        return "/login";
    }
}
