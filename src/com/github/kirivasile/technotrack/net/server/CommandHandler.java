package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.commands.*;
import com.github.kirivasile.technotrack.history.MessageStore;
import com.github.kirivasile.technotrack.session.Session;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 13.10.2015.
 */
public class CommandHandler {
    private Map<String, Command> commands;
    private DataInputStream reader;
    private DataOutputStream writer;
    private DataStore dataStore;

    public CommandHandler(InputStream reader, OutputStream writer, DataStore dataStore) {
        commands = new HashMap<>();
        commands.put(new HelpCommand().toString(), new HelpCommand());
        commands.put(new LoginCommand().toString(), new LoginCommand());
        commands.put(new UserCommand().toString(), new UserCommand());
        commands.put(new HistoryCommand().toString(), new HistoryCommand());
        commands.put(new FindCommand().toString(), new FindCommand());
        commands.put(new RegisterCommand().toString(), new RegisterCommand());
        commands.put(new UserInfoCommand().toString(), new UserInfoCommand());
        this.reader = new DataInputStream(reader);
        this.writer = new DataOutputStream(writer);
        this.dataStore = dataStore;
    }

    public void run() {
        try {
            AuthorizationService service = new AuthorizationService(dataStore.getUserStore());
            MessageStore messageStore = dataStore.getMessageStore();
            Session session = new Session(reader, writer, service, messageStore);
            while (true) {
                String command = reader.readUTF();
                if (command.equals("/exit")) {
                    break;
                }
                if (command.startsWith("/")) {
                    String[] parsedCommand = command.split("\\s+");
                    Command commandClass = commands.get(parsedCommand[0]);
                    if (commandClass == null) {
                        writer.writeUTF("Wrong command");
                        continue;
                    }
                    commandClass.run(parsedCommand, session);
                } else if (session.getCurrentUserName() != null) {
                    messageStore.addMessage(session.getCurrentUserName(), command);
                    writer.writeUTF("Message delivered");
                }
            }
            dataStore.close();
        } catch (Exception e) {
            System.err.println("Exception in reading command " + e.toString());
        }
    }
}
