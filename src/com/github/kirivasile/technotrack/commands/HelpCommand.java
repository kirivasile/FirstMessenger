package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 13.10.2015.
 */
public class HelpCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        writer.writeUTF("List of commands:\n" +
                "  /login - sign up a user\n" +
                "  /login <login> <password> - sign in with these parameters\n" +
                "  /user <nick> - change your nickname to \'nick\'\n" +
                "  /history <n> - show last n messages. Without parameter \'n\' show the full history\n" +
                "  /find <regex> - find the message using regular expression \'regex\'\n" +
                "  /exit - exit the program\n" +
                "  /help - show the list of commands");
    }

    public String toString() {
        return "/help";
    }
}
