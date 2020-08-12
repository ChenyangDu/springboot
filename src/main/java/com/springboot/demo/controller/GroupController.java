package com.springboot.demo.controller;

import com.springboot.demo.entity.*;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.GroupRepository;
import com.springboot.demo.repository.UserRepository;
import com.springboot.demo.repository.User_groupRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class GroupController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private User_groupRespository user_groupRespository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/group/create")
    public Result create(@RequestBody Group group){
        group.setId((int) (System.currentTimeMillis()%2000000011));
        System.out.println(group.toString());
        groupRepository.save(group);

        User_group_relation user_group_relation = new User_group_relation(
                new User_group_relationKey(group.getCreator_id(),group.getId())
        );
        user_groupRespository.save(user_group_relation);
        return Result.success(group);
    }

    @GetMapping("/group/member")
    public Result member(@RequestParam ("group_id") int group_id){
        List<User> list = new ArrayList<>();
        for(User_group_relation user_group_relation: user_groupRespository.findAll()){
            if(user_group_relation.getUser_group_relationKey().getGroup_id() == group_id){
                int user_id = user_group_relation.getUser_group_relationKey().getUser_id();
                Optional<User>optional = userRepository.findById(user_id);
                if(optional.isPresent()){
                    list.add(optional.get());
                }
            }
        }
        return Result.success(list);
    }

    @GetMapping("/group/document")
    public Result document(@RequestParam("group_id") int group_id){
        List<Document> list = new ArrayList<>();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("group_id",ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("id").withIgnorePaths("creator_id").withIgnorePaths("create_time")
                .withIgnorePaths("last_edit_time").withIgnorePaths("is_deleted").withIgnorePaths("is_editting")
                .withIgnorePaths("name").withIgnorePaths("edit_times").withIgnoreNullValues();
        Document document = new Document();
        document.setGroup_id(group_id);
        System.out.println(document);
        Example example = Example.of(document,matcher);
        list = documentRepository.findAll(example);
        return Result.success(list);
    }
}
