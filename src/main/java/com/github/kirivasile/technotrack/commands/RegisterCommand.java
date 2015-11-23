package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.authorization.AuthorizationService;
import com.github.kirivasile.technotrack.message.AnswerMessage;
import com.github.kirivasile.technotrack.message.Protocol;
import com.github.kirivasile.technotrack.message.SerializationProtocol;
import com.github.kirivasile.technotrack.session.Session;

import java.io.DataOutputStream;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Регистрация пользователя: /register
 */
public class RegisterCommand implements Command {
    /**
     * @param args 0 - название команды, 1 - логин, 2 - пароль
     * @param session Данные о текущей сессии
     * @throws Exception
     */
    @Override
    public void run(String[] args, Session session) throws Exception {
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        AnswerMessage.Value success;
        if (args.length == 3) {
            AuthorizationService service = session.getAuthorizationService();
            service.registerUser(args[1], args[2], session);
        } else {
            DataOutputStream writer = session.getWriter();
            success = AnswerMessage.Value.NUM_ARGS;
            writer.write(protocol.encode(new AnswerMessage("", success)));
        }
    }

    public String toString() {
        return "/register";
    }
}
