package com.github.kirivasile.technotrack.message;

import java.util.*;

/**
 * Created by Kirill on 17.11.2015.
 * GitHub profile: http://github.com/kirivasile
 * E-mail: kirivasile@yandex.ru
 */
public class FileChatStore implements ChatStore {
    private Map<Integer, Chat> chats;
    private Map<Integer, Chat> privateChats;

    public FileChatStore() {
        chats = new TreeMap<>();
        privateChats = new HashMap<>();
    }

    @Override
    public synchronized int createChat(List<Integer> participants) throws Exception {
        int chatId = chats.size() + privateChats.size();
        Chat chat = new Chat(participants, chatId);
        chats.put(chatId, chat);
        return chatId;
    }

    @Override
    public synchronized int createPrivateChat(int id1, int id2) throws Exception {
        int chatId = getPrivateChat(id1, id2);
        if (chatId != -1) {
            return -chatId - 1;
        }
        chatId = privateChats.size() + chats.size();
        List<Integer> participants = new ArrayList<>();
        participants.add(id1);
        participants.add(id2);
        Chat chat = new Chat(participants, chatId);
        privateChats.put(chatId, chat);
        return chatId;
    }

    @Override
    public synchronized int getPrivateChat(int id1, int id2) throws Exception {
        for (Map.Entry<Integer, Chat> it : privateChats.entrySet()) {
            List<Integer> userIds = it.getValue().getParticipantIds();
            if (id1 == userIds.get(0) && id2 == userIds.get(1) || id1 == userIds.get(1) && id2 == userIds.get(0)) {
                return it.getValue().getId();
            }
        }
        return -1;
    }

    @Override
    public Map<Integer, Chat> getChatList() throws Exception {
        Map<Integer, Chat> result = chats;
        for (Map.Entry<Integer, Chat> it : privateChats.entrySet()) {
            chats.put(it.getKey(), it.getValue());
        }
        return result;
    }

    @Override
    public Chat getChat(Integer id) throws Exception {
        //return chats.get(id);
        Chat result = chats.get(id);
        if (result == null) {
            return privateChats.get(id);
        }
        return result;
    }

    public boolean isAPrivateChat(Integer id) throws Exception {
        return privateChats.containsKey(id);
    }
}
