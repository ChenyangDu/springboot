package com.springboot.demo.controller;

import com.springboot.demo.entity.User;
import com.springboot.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public String login(@RequestBody User user){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("phone", match -> match.contains())//模糊查询
                .withMatcher("password", match -> match.contains())
                .withIgnorePaths("id");
        Example <User> example = Example.of(user,matcher);
        List<User> matchUsers = userRepository.findAll(example);
        for(User matchUser :matchUsers){
            if(matchUser.getPhone().equals(user.getPhone()) &&
            matchUser.getPassword().equals(user.getPassword())){
                return matchUser.toString();//成功的返回
            }
        }
        return "error";//不成功的返回
    }

    @PostMapping("/register")
    public String register(@RequestBody User user){
        System.out.println("reg");
        for(User matchUser : findAll()){
            if(matchUser.getPhone() != null && matchUser.getPhone().equals(user.getPhone())){
                return "phoneExits";
            }
            if(matchUser.getWechat() != null && matchUser.getWechat().equals(user.getWechat())){
                return "wechatExits";
            }
            if(matchUser.getQq() != null && matchUser.getQq().equals(user.getQq())){
                return "qqExits";
            }
        }
        userRepository.save(user);
        return "ok";
    }
}