package com.github.kirivasile.technotrack;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.authorization.UserStore;
import com.github.kirivasile.technotrack.commands.Command;
import com.github.kirivasile.technotrack.commands.HelpCommand;
import com.github.kirivasile.technotrack.commands.LoginCommand;
import com.github.kirivasile.technotrack.commands.UserCommand;
import com.github.kirivasile.technotrack.session.Session;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kirill on 13.10.2015.
 */
public class CommandHandler {
    private Map<String, Command> commands;

    public CommandHandler() {
        commands = new HashMap<>();
        commands.put(new HelpCommand().toString(), new HelpCommand());
        commands.put(new LoginCommand().toString(), new LoginCommand());
        commands.put(new UserCommand().toString(), new UserCommand());
    }

    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            UserStore userStore = new UserStore();
            AuthorizationService service = new AuthorizationService(userStore);
            Session session = new Session(reader, service);
            while (true) {
                System.out.print("$ ");
                String command = reader.readLine();
                if (command.equals("/exit")) {
                    break;
                }
                if (command.startsWith("/")) {
                    String[] parsedCommand = command.split("\\s+");
                    Command commandClass = commands.get(parsedCommand[0]);
                    if (commandClass == null) {
                        System.out.println("Wrong command");
                        continue;
                    }
                    commandClass.run(parsedCommand, session);
                }
            }
            userStore.close();
        } catch (Exception e) {
            System.err.println("Exception in reading command " + e.toString());
        }
    }
}
