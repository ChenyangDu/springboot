package com.springboot.demo.controller;

import com.springboot.demo.entity.Authority_user;
import com.springboot.demo.entity.Authority_userKey;
import com.springboot.demo.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin
@RestController
public class AuthorityController {
    @Autowired
    private AuthorityRepository authorityRepository;

    @GetMapping("/authority")
    public Result authorith(@RequestParam("user_id") Integer user_id,
                            @RequestParam("doc_id") Integer doc_id){
        Optional<Authority_user> optional = authorityRepository.findById(new Authority_userKey(0,doc_id));
        if(optional.isPresent()){
            return Result.success(optional.get());
        }else{
            //0号用户代表所有用户
            optional = authorityRepository.findById(new Authority_userKey(user_id,doc_id));
            if(optional.isPresent()){
                return Result.success(optional.get());
            }
            return Result.error(400,"用户/文章不存在");
        }
    }
}
