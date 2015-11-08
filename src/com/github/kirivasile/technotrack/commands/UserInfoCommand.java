package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.authorization.User;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 09.11.2015.
 */
public class UserInfoCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        if (args.length == 1) {
            String currentUserName = session.getCurrentUserName();
            if (currentUserName == null) {
                writer.writeUTF("Please login before using this command");
            } else {
                AuthorizationService service = session.getAuthorizationService();
                User user = service.getUserInfo(currentUserName);
                if (user == null) {
                    writer.writeUTF("Can't find you. Don't worry.");
                } else {
                    String data = String.format("Username: %s," +
                                                " nickname: %s", user.getName(), user.getPassword(), user.getNick());
                    writer.writeUTF(data);
                }
            }
        } else if (args.length == 2) {
            AuthorizationService service = session.getAuthorizationService();
            User user = service.getUserInfo(args[1]);
            if (user == null) {
                writer.writeUTF("Can't find user: " + args[1]);
            } else {
                String data = String.format("Username: %s," +
                        " nickname: %s", user.getName(), user.getNick());
                writer.writeUTF(data);
            }
        } else {
            writer.writeUTF("Wrong number of arguments");
        }
    }

    public String toString() {
        return "/user_info";
    }
}
