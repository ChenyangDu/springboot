package com.springboot.demo.controller;

import com.springboot.demo.entity.Document;
import com.springboot.demo.entity.Group;
import com.springboot.demo.entity.User;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.GroupRepository;
import com.springboot.demo.repository.UserRepository;
import lombok.Data;
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
    @Autowired
    private GroupRepository groupRepository;

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
            if(user.getEmail()!= null && user.getEmail().equals(key)){
                result.add(user);
            }else if (user.getPhone()!=null && user.getPhone().equals(key)){
                result.add(user);
            }
        }
        return Result.success(result);
    }

    @GetMapping("/group")
    public Result group(@RequestParam("key") String key){
        List<Group> list = groupRepository.findAll();
        List<GroupFuck> result = new ArrayList<>();
        for(Group group : list){
            if(group.getName() != null && group.getName().contains(key) &&!group.isDismissed()){
                result.add(new GroupFuck(group));
            }
        }
        System.out.println(result.toString());
        return Result.success(result);
    }

    @Data
    private class GroupFuck extends Group{
        private String creator_name;
        public GroupFuck(Group group){
            User user = userRepository.findById(group.getCreator_id()).orElse(null);
            if(user != null){
                creator_name = user.getName();
            }
            id = group.getId();
            creator_id = group.getCreator_id();
            name = group.getName();
        }
    }

}
