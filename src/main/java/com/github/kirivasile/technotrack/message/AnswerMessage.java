package com.github.kirivasile.technotrack.message;

import java.io.Serializable;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Ответ сервера на запрос клиента
 */
public class AnswerMessage implements Serializable {
    /**
     * Тип ответа
     */
    public enum Value {
        SUCCESS,
        ERROR,
        LOGIN,
        NUM_ARGS,
        CHAT,
    }

    /**
     * Данные об ответе
     */
    private int id;
    private String from;
    private String message;
    private Value result;

    public AnswerMessage(String message, Value result) {
        this.message = message;
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Value getResult() {
        return result;
    }

    public void setResult(Value result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
