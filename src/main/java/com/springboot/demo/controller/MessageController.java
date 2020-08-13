package com.springboot.demo.controller;

import com.springboot.demo.entity.Message;
import com.springboot.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequestMapping("/message")
@RestController
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @GetMapping("/user")
    private Result user(@RequestParam("user_id") Integer user_id){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("receive_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("id").withIgnorePaths("sender_id").withIgnorePaths("docu_id")
                .withIgnorePaths("group_id").withIgnorePaths("have_read").withIgnorePaths("message_type");

        Message message = new Message();
        message.setReceiver_id(user_id);
        Example example = Example.of(message,matcher);
        return Result.success(messageRepository.findAll(example));
    }
}
