package com.github.kirivasile.technotrack.message;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class CommandMessage extends Message {
    public CommandMessage() {
        this.type = MessageType.COMMAND;
    }
}
