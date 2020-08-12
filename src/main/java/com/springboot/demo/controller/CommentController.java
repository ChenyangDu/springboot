package com.springboot.demo.controller;

import com.springboot.demo.entity.Comment;
import com.springboot.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin
@RestController
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/comment/create")
    public Result create(@RequestBody Comment comment){
        comment.setComment_id((int) (System.currentTimeMillis()%2000000011));
        comment.setTime(new Date());
        System.out.println(comment);
        commentRepository.save(comment);
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
        return Result.success(commentRepository.findAll(example));
    }
}
