package com.github.kirivasile.technotrack.message;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Протокол сериализации для отправки сообщения между клиентом и сервером
 * @param <T> Message или AnswerMessage
 */
public interface Protocol<T> {
    /**
     * Закодировать сообщение
     * @param msg Информация о сообщении
     * @return Двоичная кодировка
     * @throws Exception
     */
    byte[] encode(T msg) throws Exception;

    /**
     * Раскодировать сообщение
     * @param data Двоичная кодировка
     * @return Информация о сообщении
     * @throws Exception
     */
    T decode(byte[] data) throws Exception;
}
