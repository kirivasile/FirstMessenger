package com.github.kirivasile.technotrack.message;


import java.io.Serializable;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Информация о сообщении
 */
public class Message implements Serializable {
    /**
     * Идентификатор сообщения
     */
    protected int id;

    /**
     * Идентификатор автора
     */
    protected int authorId;

    /**
     * Сообщение
     */
    protected String message;

    public Message() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String toString() {
        return String.format("Author %d: %s", authorId, message);
    }
}
