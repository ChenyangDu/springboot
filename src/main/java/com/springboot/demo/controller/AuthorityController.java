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

    @GetMapping("/authority/share")//给所有人的权限
    public Result share(@RequestParam("document_id") int doc_id,@RequestParam("can_read")boolean can_read,
                        @RequestParam("can_comment") boolean can_comment,
                        @RequestParam("can_edit") boolean can_edit){
        Authority_user authority_user = new Authority_user();
        authority_user.setAuthority_userKey(new Authority_userKey(0,doc_id));
        authority_user.setCan_read(can_read);
        authority_user.setCan_comment(can_comment);
        authority_user.setCan_edit(can_edit);
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
        return Result.success();
    }
}
