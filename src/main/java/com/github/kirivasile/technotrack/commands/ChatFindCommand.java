package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.message.*;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Команда, отвечающая за поиск сообщения в чате, соответствующих данному регулярному выражению
 */
public class ChatFindCommand implements Command {
    /**
     * @param args 0 - название команды, 1 - идентификатор чата, 2,3,4... - регулярное выражение
     * @param session Данные о текущей сессии
     * @throws Exception
     */
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (session.getCurrentUserName() == null) {
            success = AnswerMessage.Value.LOGIN;
            writer.write(protocol.encode(new AnswerMessage(message, success)));
            return;
        }
        if (args.length >= 3) {
            int chatId = Integer.parseInt(args[1]);
            ChatStore chatStore = session.getDataStore().getChatStore();
            Chat chat = chatStore.getChat(chatId);
            if (chat == null) {
                message = "Chat not found";
                success = AnswerMessage.Value.ERROR;
                writer.write(protocol.encode(new AnswerMessage(message, success)));
                return;
            }
            StringBuilder regexBuilder = new StringBuilder();
            for (int i = 2; i < args.length; ++i) {
                regexBuilder.append(args[i]);
                if (i != args.length - 1) {
                    regexBuilder.append(' ');
                }
            }
            String regex = regexBuilder.toString();
            StringBuilder stringBuilder = new StringBuilder();
            MessageStore messageStore = chat.getMessageStore();
            List<Message> messages = messageStore.getMessageByRegex(regex);
            for (Message it : messages) {
                stringBuilder.append(it.toString());
                stringBuilder.append("\n");
            }
            message = stringBuilder.toString();
            success = AnswerMessage.Value.SUCCESS;
        } else {
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    @Override
    public String toString() {
        return "/chat_find";
    }
}
