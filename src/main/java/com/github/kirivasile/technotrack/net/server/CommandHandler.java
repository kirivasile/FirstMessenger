package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.commands.*;
import com.github.kirivasile.technotrack.message.*;
import com.github.kirivasile.technotrack.session.Session;
import com.github.kirivasile.technotrack.session.SessionManager;

import java.io.*;
import java.util.Map;

/**
 * Created by Kirill on 13.10.2015.
 */
public class CommandHandler implements Runnable{
    private DataInputStream reader;
    private DataOutputStream writer;
    private DataStore dataStore;
    private SessionManager sessionManager;

    public CommandHandler(InputStream reader, OutputStream writer, DataStore dataStore, SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.reader = new DataInputStream(reader);
        this.writer = new DataOutputStream(writer);
        this.dataStore = dataStore;
    }

    @Override
    public void run() {
        try {
            Map<String, Command> commands = dataStore.getCommandsStore();
            AuthorizationService service = new AuthorizationService(dataStore.getUserStore());
            Session session = new Session(reader, writer, service, dataStore, sessionManager);
            sessionManager.addSession(session);
            Protocol<AnswerMessage> answerProtocol = new SerializationProtocol<>();
            Protocol<Message> readProtocol = new SerializationProtocol<>();
            while (true) {
                byte[] readData = new byte[1024 * 64];
                reader.read(readData);
                Message received = readProtocol.decode(readData);
                String command = received.getMessage();
                if (command.equals("/exit")) {
                    break;
                }
                String[] parsedCommand = command.split("\\s+");
                Command commandClass = commands.get(parsedCommand[0]);
                if (commandClass == null) {
                    String str = "Wrong command";
                    AnswerMessage answer = new AnswerMessage(str , AnswerMessage.Value.ERROR);
                    writer.write(answerProtocol.encode(answer));
                    continue;
                }
                commandClass.run(parsedCommand, session);

            }

            // FIXME: вроде если закроете в одном потоке, дата стор будет закрыт для всех. Нехорошо
            // Исправил
        } catch (Exception e) {
            System.err.println("Exception in reading command " + e.toString());
        }
    }
}
