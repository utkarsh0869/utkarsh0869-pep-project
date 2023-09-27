package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    
    public MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message addMessage(Message message) {
        if(!message.getMessage_text().isBlank() && !message.getMessage_text().trim().isEmpty() && 
            message.getMessage_text().length() < 255) {

                if(messageDAO.checkIfUserExistsBy_PostedBy(message.getPosted_by())) {
                    int messageId = messageDAO.createMessage(message);

                    message.setMessage_id(messageId);

                    return message;
                } else {
                    return null;
                }

        } else {
            return null;
        }
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId) {
        // Attempt to delete the message from the database
        Message deletedMessage = messageDAO.deleteMessage(messageId);

        return deletedMessage;
    }

    public void updateMessage(Message existingMessage) {
        messageDAO.updateMessage(existingMessage);
    }

    public List<Message> getMessagesByAccountId(int accountId) {
        return messageDAO.getMessagesByAccountId(accountId);
    }

}
