package com.springboot.demo.controller;

import com.springboot.demo.entity.Document;
import com.springboot.demo.entity.Favorite;
import com.springboot.demo.entity.User;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.FavoriteRepository;
import com.springboot.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @GetMapping("/users")
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("phone", match -> match.contains())//模糊查询
                .withMatcher("password", match -> match.contains())
                .withIgnorePaths("id");
        Example <User> example = Example.of(user,matcher);
        List<User> matchUsers = userRepository.findAll(example);
        for(User matchUser :matchUsers){
            if(matchUser.getPhone().equals(user.getPhone()) &&
            matchUser.getPassword().equals(user.getPassword())){
                System.out.println(Result.success(matchUser).toString());
                return Result.success(matchUser);//成功的返回
            }
        }

        return Result.error(400,"账号或密码错误");//不成功的返回
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        for(User matchUser : findAll()){
            if(matchUser.getPhone() != null && matchUser.getPhone().equals(user.getPhone())){
                return Result.error(200,"手机号已被使用");
            }
            if(matchUser.getWechat() != null && matchUser.getWechat().equals(user.getWechat())){
                return Result.error(200,"微信已被使用");
            }
            if(matchUser.getQq() != null && matchUser.getQq().equals(user.getQq())){
                return Result.error(200,"QQ已被使用");
            }
        }
        userRepository.save(user);
        return Result.success();
    }

    @GetMapping("/user/info")
    public Result userInfo(@RequestParam("id") int id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            return Result.success(optionalUser.get());
        }else{
            return Result.error(400,"用户不存在");
        }
    }

    @PostMapping("/user/save")
    public Result userSave(@RequestBody User user){
        userRepository.save(user);
        return Result.success();
    }

    @GetMapping("/user/own")
    public Result userOwn(@RequestParam("id") int id){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            Document tmpDocu=new Document();
            tmpDocu.setCreator_id(id);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withMatcher("creator_id", ExampleMatcher.GenericPropertyMatcher::exact)
                    .withIgnorePaths("id")
                    .withIgnorePaths("group_id")
                    .withIgnorePaths("create_time")
                    .withIgnorePaths("last_edit_time")
                    .withIgnorePaths("is_deleted")
                    .withIgnorePaths("is_editting")
                    .withIgnorePaths("name");
            Example <Document> example = Example.of(tmpDocu,matcher);
            List<Document> myDocus=documentRepository.findAll(example);
            return Result.success(myDocus);
        }else{
            return Result.error(400,"用户不存在");
        }
    }

    @GetMapping("/user/favorite")
    public Result userFavorite(@RequestParam("id") int id){
        List<Favorite> result = new ArrayList<>();
        for(Favorite favorite :favoriteRepository.findAll()){
            if (favorite.getFavorityKey().getUser_id() == id) {
                result.add(favorite);
            }
        }
        return Result.success(result);
    }
}
