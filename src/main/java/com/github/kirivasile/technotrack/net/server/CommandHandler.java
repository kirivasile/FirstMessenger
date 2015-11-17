package com.github.kirivasile.technotrack.net.server;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.commands.*;
import com.github.kirivasile.technotrack.message.*;
import com.github.kirivasile.technotrack.session.Session;

import java.io.*;
import java.util.Map;

/**
 * Created by Kirill on 13.10.2015.
 */
public class CommandHandler {
    private DataInputStream reader;
    private DataOutputStream writer;
    private DataStore dataStore;

    public CommandHandler(InputStream reader, OutputStream writer, DataStore dataStore) {
        this.reader = new DataInputStream(reader);
        this.writer = new DataOutputStream(writer);
        this.dataStore = dataStore;
    }

    public void handle() {
        try {
            Map<String, Command> commands = dataStore.getCommandsStore();
            AuthorizationService service = new AuthorizationService(dataStore.getUserStore());
            MessageStore fileMessageStore = dataStore.getMessageStore();
            Session session = new Session(reader, writer, service, dataStore);
            Protocol<AnswerMessage> answerProtocol = new SerializationProtocol<AnswerMessage>();
            Protocol<Message> readProtocol = new SerializationProtocol<>();
            while (true) {
                //String command = reader.readUTF();
                byte[] readData = new byte[1024 * 64];
                reader.read(readData);
                Message received = readProtocol.decode(readData);
                if (received.getType() == Message.MessageType.COMMAND) {
                    String command = received.getMessage();
                    if (command.equals("/exit")) {
                        break;
                    }
                    String[] parsedCommand = command.split("\\s+");
                    Command commandClass = commands.get(parsedCommand[0]);
                    if (commandClass == null) {
                        //writer.writeUTF("Wrong command");
                        String str = "Wrong command";
                        AnswerMessage answer = new AnswerMessage(str , AnswerMessage.Value.ERROR);
                        writer.write(answerProtocol.encode(answer));
                        continue;
                    }
                    commandClass.run(parsedCommand, session);
                } else if (session.getCurrentUserName() != null) {
                    fileMessageStore.addMessage(session.getCurrentUserName(), received.getMessage());
                    //writer.writeUTF("Message delivered");
                    String str = "Message delivered";
                    AnswerMessage answer = new AnswerMessage(str, AnswerMessage.Value.SUCCESS);
                    writer.write(answerProtocol.encode(answer));
                } else {
                    //writer.writeUTF("Please login or sign up first");
                    String str = "Please login or sign up first";
                    AnswerMessage answer = new AnswerMessage(str, AnswerMessage.Value.ERROR);
                    writer.write(answerProtocol.encode(answer));
                }

            }

            // FIXME: вроде если закроете в одном потоке, дата стор будет закрыт для всех. Нехорошо
            //dataStore.close();
        } catch (Exception e) {
            System.err.println("Exception in reading command " + e.toString());
        }
    }
}
