package com.github.kirivasile.technotrack.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        //readDataFromFile();
    }

    public void readDataFromFile() {
        File directory = new File("messages");
        if (!directory.exists() || !directory.isDirectory()) {
            System.err.println("FileChatStore: failed to open directory");
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.getName().startsWith("data")) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                    String line = reader.readLine();
                    String[] args = line.split("\\s+");
                    int chatId = Integer.parseInt(args[0]);
                    List<Integer> participants = new ArrayList<>();
                    int size = Integer.parseInt(args[1]);
                    for (int i = 0; i < size; ++i) {
                        participants.add(Integer.parseInt(args[i + 2]));
                    }
                    if (participants.size() == 2) {
                        createPrivateChatWithId(chatId, participants.get(0), participants.get(1));
                    } else {
                        createChatWithId(chatId, participants);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("FileMessageStore: Incorrect chat data");
                } catch (Exception e) {
                    System.err.println("FileMessageStore: Error in reading from file: " + e.toString());
                }
            }
        }
    }

    private void createPrivateChatWithId(int chatId, int userId1, int userId2) {
        List<Integer> participants = new ArrayList<>();
        participants.add(userId1);
        participants.add(userId2);
        Chat chat = new Chat(participants, chatId);
        privateChats.put(chatId, chat);
    }

    private void createChatWithId(int chatId, List<Integer> participants) {
        Chat chat = new Chat(participants, chatId);
        chats.put(chatId, chat);
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
        Chat result = chats.get(id);
        if (result == null) {
            return privateChats.get(id);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        /*for (Map.Entry<Integer, Chat> pair : chats.entrySet()) {
            pair.getValue().close();
        }*/
    }
}
