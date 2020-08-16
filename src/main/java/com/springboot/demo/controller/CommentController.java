package com.springboot.demo.controller;

import com.springboot.demo.entity.*;
import com.springboot.demo.repository.CommentRepository;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.MessageRepository;
import com.springboot.demo.repository.UserRepository;
import com.springboot.demo.tool.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/comment/create")
    public Result create(@RequestBody Comment comment){
        comment.setComment_id((int) (System.currentTimeMillis()%2000000011));
        comment.setTime(Global.nowTime());
        System.out.println(comment);
        commentRepository.save(comment);

        //消息相关
        Message message = new Message();
        message.setId((int) (System.currentTimeMillis()%2000000011));
        Optional<Document> optional = documentRepository.findById(comment.getDocument_id());
        if(!optional.isPresent()){
            return Result.error(400,comment.getDocument_id()+"文章没有创建者");
        }
        message.setReceiver_id(optional.get().getCreator_id());
        message.setSender_id(comment.getUser_id());
        message.setDocu_id(comment.getDocument_id());
        message.setGroup_id(null);
        message.setHave_read(false);
        message.setMessage_type(MessageType.COMMENT.ordinal());
        messageRepository.save(message);

        return Result.success(comment);
    }

    @GetMapping("/comment/list")
    public Result list(@RequestParam("doc_id") Integer doc_id){
        Comment comment = new Comment();
        comment.setDocument_id(doc_id);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("doc_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("comment_id");
        Example<Comment>example = Example.of(comment,matcher);
        List<Comment> list = commentRepository.findAll(example);
        for(Comment comment1 : list){
            fuck(comment1);
        }
        return Result.success(list);
    }

    public Comment fuck(Comment comment){
        User user = userRepository.findById(comment.getUser_id()).orElse(null);
        if(user != null){
            comment.setUsername(user.getName());
        }
        return comment;
    }
}
