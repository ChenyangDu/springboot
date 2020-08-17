package com.springboot.demo.controller;

import com.springboot.demo.entity.Document;
import com.springboot.demo.entity.Group;
import com.springboot.demo.entity.Message;
import com.springboot.demo.entity.User;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.GroupRepository;
import com.springboot.demo.repository.MessageRepository;
import com.springboot.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private GroupRepository groupRepository;

    @GetMapping("/user")
    private Result user(@RequestParam("user_id") Integer user_id){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("receive_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("id").withIgnorePaths("sender_id").withIgnorePaths("docu_id")
                .withIgnorePaths("group_id").withIgnorePaths("have_read").withIgnorePaths("message_type")
                .withIgnorePaths("time").withIgnorePaths("operate");

        Message message = new Message();
        message.setReceiver_id(user_id);
        Example example = Example.of(message,matcher);
        List <Message> list = messageRepository.findAll(example);
        for(Message message1 : list){
            fuck(message1);
        }
        return Result.success(list);
    }

    @PostMapping("/confirm")
    private Result confirm(@RequestParam("msg_id") Integer id){
        Optional<Message> optionalMessage=messageRepository.findById(id);
        Message tmpMsg=optionalMessage.get();
        tmpMsg.setHave_read(true);
        messageRepository.save(tmpMsg);
        return Result.success();
    }



    private Message fuck(Message message){
        if(message.getReceiver_id() != null) {
            User user = userRepository.findById(message.getReceiver_id()).orElse(null);
            if (user != null) {
                message.setReceiver_name(user.getName());
            }
        }
        if(message.getSender_id() != null) {
            User user = userRepository.findById(message.getSender_id()).orElse(null);
            if (user != null) {
                message.setSender_name(user.getName());
            }
        }
        if(message.getGroup_id() != null){
            Group group = groupRepository.findById(message.getGroup_id()).orElse(null);
            if(group != null){
                message.setGroup_name(group.getName());
            }
        }
        if(message.getDocu_id() != null){
            Document document = documentRepository.findById(message.getDocu_id()).orElse(null);
            if(document != null){
                message.setDocu_name(document.getName());
            }
        }
        return message;
    }
}
