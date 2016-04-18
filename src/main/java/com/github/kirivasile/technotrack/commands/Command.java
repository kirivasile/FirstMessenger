package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.session.Session;

import java.io.BufferedReader;

/**
 * Created by Kirill on 16.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */

/**
 * Общий интерфейс команды пользователя
 */
public interface Command {
    /**
     * Запуск команды с параметрами
     * @param args Аргументы
     * @param session Данные о текущей сессии
     * @throws Exception
     */
    void run(String[] args, Session session) throws Exception; //args[0] - is the name of the command

    String toString();
}
