package DAO;

import java.util.List;

import Model.Message;

public interface MessageDAO {

    List<Message> getMessages();

    void addMessage(Message message);
    void deleteMessage(Message message);
    void updateMessage(Message message);
}