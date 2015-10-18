package com.github.kirivasile.technotrack.commands;

import com.github.kirivasile.technotrack.session.Session;

/**
 * Created by Kirill on 18.10.2015.
 */
public class HistoryCommand implements Command {
    @Override
    public void run(String[] args, Session session) throws Exception {

    }

    @Override
    public String toString() {
        return "/history";
    }
}
