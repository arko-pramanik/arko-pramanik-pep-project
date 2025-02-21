package Service;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public Message addMessage(Message message){
        String messageText = message.getMessage_text();
        if (messageText.isBlank() || messageText.length() >= 255){
            return null;
        }
        return messageDAO.insertMessage(message);
    }
}
