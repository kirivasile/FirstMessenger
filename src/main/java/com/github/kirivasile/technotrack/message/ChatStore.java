package com.github.kirivasile.technotrack.message;

import com.github.kirivasile.technotrack.message.Chat;

import java.util.List;
import java.util.Map;


/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public interface ChatStore {
    int createChat(List<Integer> participants) throws Exception;
    Map<Integer, Chat> getChatList() throws Exception;
    Chat getChat(Integer id) throws Exception;
}
