package com.springboot.demo.controller;

import com.springboot.demo.entity.Document;
import com.springboot.demo.entity.User;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/document")
    public Result document(@RequestParam("key") String key){
        List<Document> list = documentRepository.findAll();
        List<Document> result = new ArrayList<>();
        for(Document document : list){
            if(document.getName() != null && document.getName().contains(key)){
                result.add(document);
            }
        }
        return Result.success(result);
    }

    @GetMapping("/user")
    public Result user(@RequestParam("key") String key){
        List<User> list = userRepository.findAll();
        List<User> result = new ArrayList<>();
        for(User user : list){
            if(user.getEmail().equals(key) || user.getPhone().equals(key)){
                result.add(user);
            }
        }
        return Result.success(result);
    }

}
