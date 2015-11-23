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
 * Команда, отвечающая за смену пароля: /user_pass
 */
public class ChangePasswordCommand implements Command {

    /**
     * @param args 0 - название команды, 1 - старый пароль, 2 - новый пароль
     * @param session Текущая сессия
     * @throws Exception
     */
    @Override
    public void run(String[] args, Session session) throws Exception {
        DataOutputStream writer = session.getWriter();
        Protocol<AnswerMessage> protocol = new SerializationProtocol<>();
        String message = "";
        AnswerMessage.Value success;
        if (args.length == 3) {
            int currentUserId = session.getCurrentUserId();
            if (currentUserId == -1) {
                success = AnswerMessage.Value.LOGIN;
            } else {
                String oldPassword = args[1];
                String newPassword = args[2];
                AuthorizationService service = session.getAuthorizationService();
                int result = service.changePassword(currentUserId, oldPassword, newPassword);
                if (result == 1) {
                    message = "Old password isn't correct";
                    success = AnswerMessage.Value.ERROR;
                } else if (result == 0) {
                    message = "Password was successfully changed";
                    success = AnswerMessage.Value.SUCCESS;
                } else {
                    message = "Can't find you. Don't worry. It happens";
                    success = AnswerMessage.Value.ERROR;
                }
            }
        } else {
            success = AnswerMessage.Value.NUM_ARGS;
        }
        writer.write(protocol.encode(new AnswerMessage(message, success)));
    }

    public String toString() {
        return "/user_pass";
    }
}
