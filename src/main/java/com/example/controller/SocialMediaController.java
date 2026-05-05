package com.example.controller;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    AccountRepository acRepo;

    @Autowired
    AccountService accountService;

    @Autowired
    MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<?> toRegister(@RequestBody Account user){
        String username=user.getUsername();
        String password=user.getPassword();
        try{
            if(username==null || username.trim().isEmpty()){
                return ResponseEntity.status(400).body("Username not exist");
            }
            else if(acRepo.findByUsername(username)!=null) {
                return ResponseEntity.status(409).body("Username already exist");
            }
            else if( password==null||password.length()<4 ){
                return ResponseEntity.status(400).body("Invalid Password");
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
             Account savedUser= accountService.addUser(user);
            
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> toLogin(@RequestBody Account account){
        Account user=acRepo.findByUsername(account.getUsername());
        try{
            if(user==null){
                return ResponseEntity.status(401).body("Invalid Username");
            }
            String username=user.getUsername();
            String password=user.getPassword();
        
            if(password==null){
                return ResponseEntity.status(401).body("Invalid Password");
            }
            if(!(username.equals(account.getUsername())) || !(password.equals(account.getPassword()))){        
                return ResponseEntity.status(401).body("Invalid Username and Password");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("\n\n\n "+e.getMessage()+"\n\n\n");
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message msg){
        Account user=acRepo.findByAccountId(msg.getPostedBy());
        try{
            if((user==null)||(msg.getMessageText().isEmpty())||(msg.getMessageText().length()>255)){
                return ResponseEntity.status(400).body("The Error is from Client Side!");
            }
            else{
           return ResponseEntity.ok(messageService.createMessage(msg));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessage(){
        return ResponseEntity.ok(messageService.getAllMessage());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable int messageId){
        Message msg=messageService.getMessageById(messageId);
        return ResponseEntity.ok(msg);
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessageById(@PathVariable int messageId){
        Message msg=messageService.getMessageById(messageId);

        //Before of all messages

        int before=messageService.countAllMessage();

        if(msg!=null){
          messageService.deleteMessage(msg);
        }
        else{
        return ResponseEntity.status(200).body("");
        }

        //After of all operation
        int after=messageService.countAllMessage();

        return ResponseEntity.status(200).body(before-after);
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@RequestBody Message message,@PathVariable int messageId){
       
 
        try{
             Message msg=messageService.getMessageById(messageId);

         if(msg==null){
            return ResponseEntity.status(400).body("The message is not exists...");
        }
        else if(message==null || message.getMessageText().isEmpty()){
            return ResponseEntity.status(400).body("The new message is null...");
        }
        else if(message.getMessageText().length()>255){
            return ResponseEntity.status(400).body("New message Length is too high...");
        }
            return messageService.updateMessage(message,messageId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<?> getMessageByAccount(@PathVariable int accountId){
        try{
            List<Message> list=messageService.getAllMessageByAccountId(accountId);
            for(Message m:list){
                System.out.println(m);
            }
        return ResponseEntity.ok(list);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.ok().body("null");
    }
}
