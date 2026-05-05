package com.example.service;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public Message createMessage(Message msg) {
        return messageRepository.save(msg);
    }

    public List<Message> getAllMessage() {
        return messageRepository.findAll();
    }

    public int countAllMessage(){
        return messageRepository.findAll().size();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findByMessageId(messageId);  
    }

    public void deleteMessage(Message message){
        
         try{
            messageRepository.delete(message);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> updateMessage(Message message, int messageId) {
        int c=messageRepository.updateMessage(message.getMessageText(),messageId);
        System.out.println("Number of changed: "+c);
        return ResponseEntity.ok(c);
    }

     public List<Message> getAllMessageByAccountId(int id) {
       //Account ac=acRepo.findByAccountId(id);
       return messageRepository.findAllByPostedBy(id);
    }
}
