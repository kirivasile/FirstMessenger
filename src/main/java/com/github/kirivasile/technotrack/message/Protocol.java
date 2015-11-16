package com.github.kirivasile.technotrack.message;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public interface Protocol<T> {
    byte[] encode(T msg) throws Exception;

    T decode(byte[] data) throws Exception;
}
