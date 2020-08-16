package com.springboot.demo.controller;

import com.springboot.demo.entity.Authority_user;
import com.springboot.demo.entity.Authority_userKey;
import com.springboot.demo.entity.User;
import com.springboot.demo.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class AuthorityController {
    @Autowired
    private AuthorityRepository authorityRepository;

    private Authority_user authorityOne (Integer user_id,Integer doc_id){
        Authority_user au = new Authority_user(new Authority_userKey(user_id,doc_id),false,false,false,false);
        Optional<Authority_user> optional = authorityRepository.findById(new Authority_userKey(0,doc_id));
        if(optional.isPresent()){
            au = optional.get();
        }

        //0号用户代表所有用户
        optional = authorityRepository.findById(new Authority_userKey(user_id,doc_id));
        if(optional.isPresent()){
            Authority_user authority_user = optional.get();
            au.setCan_edit(au.isCan_edit() || authority_user.isCan_edit());
            au.setCan_comment(au.isCan_comment() || authority_user.isCan_comment());
            au.setCan_read(au.isCan_read()||authority_user.isCan_read());
        }
        return au;
    }

    @GetMapping("/authority")
    public Result authority(@RequestParam("user_id") Integer user_id,
                            @RequestParam("doc_id") Integer doc_id){
         return Result.success(authorityOne(user_id,doc_id));
    }

    @GetMapping("/authority/share")//给所有人的权限
    public Result share(@RequestParam("document_id") int doc_id,@RequestParam("can_read")boolean can_read,
                        @RequestParam("can_comment") boolean can_comment,
                        @RequestParam("can_edit") boolean can_edit){
        Authority_user authority_user = new Authority_user();
        authority_user.setAuthority_userKey(new Authority_userKey(0,doc_id));
        authority_user.setCan_read(can_read);
        authority_user.setCan_comment(can_comment);
        authority_user.setCan_edit(can_edit);
        authorityRepository.save(authority_user);
        return Result.success();
    }

    @GetMapping("/authority/setuser")
    public Result setuser(@RequestParam("document_id")int doc_id,
                          @RequestParam("user_id")int user_id,
                          @RequestParam("can_read")boolean can_read,
                          @RequestParam("can_comment") boolean can_comment,
                          @RequestParam("can_edit") boolean can_edit){
        Authority_user authority_user = new Authority_user();
        authority_user.setAuthority_userKey(new Authority_userKey(user_id,doc_id));
        authority_user.setCan_read(can_read);
        authority_user.setCan_comment(can_comment);
        authority_user.setCan_edit(can_edit);
        authorityRepository.save(authority_user);
        return Result.success();
    }

    @GetMapping("/authority/users")
    public Result users(@RequestParam("doc_id")int doc_id, @RequestParam("users")Integer users_id[]){
        List<Integer> list = new ArrayList<Integer>();
        for(Integer user_id : users_id){
            Authority_user authority_user = authorityOne(user_id,doc_id);
            if(authority_user.isCan_edit()){
                list.add(3);
            }else if(authority_user.isCan_comment()){
                list.add(2);
            }else if(authority_user.isCan_read()){
                list.add(1);
            }else {
                list.add(0);
            }
        }
        return Result.success(list);
    }
}
