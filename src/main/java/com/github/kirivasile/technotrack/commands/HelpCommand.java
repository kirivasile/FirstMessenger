package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 13.10.2015.
 */
public class HelpCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {
        //Немного устарел
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "List of commands:\n" +
                "  /register <login> <password> - sign up a user\n" +
                "  /login <login> <password> - sign in with these parameters\n" +
                "  /user <nick> - change your nickname to \'nick\'\n" +
                "  /user_info - get the information about you\n" +
                "  /user_info <nick> - get the information about \'nick\'\n" +
                "  /user_pass <old_pass> <new_pass> - changes the password from \'old_pass\' to \'new_pass\'\n" +
                "  /chat_history <n> - show messages in \'n\' chat\n" +
                "  /find <regex> - find the message using regular expression \'regex\'\n" +
                "  /exit - exit the program\n" +
                "  /help - show the list of commands";
        writer.write(protocol.encode(new AnswerMessage(message, AnswerMessage.Value.SUCCESS)));
    }

    public String toString() {
        return "/help";
    }
}
