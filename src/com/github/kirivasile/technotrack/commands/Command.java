package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.session.Session;

import java.io.BufferedReader;

/**
 * Created by Kirill on 13.10.2015.
 */
public interface Command {
    void run(String[] args, Session session) throws Exception; //args[0] - is the name of the command

    String toString();
}