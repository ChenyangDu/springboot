package com.springboot.demo.controller;

import com.springboot.demo.entity.Authority_user;
import com.springboot.demo.entity.Authority_userKey;
import com.springboot.demo.entity.Document;
import com.springboot.demo.entity.User;
import com.springboot.demo.repository.AuthorityRepository;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.UserRepository;
import com.springboot.demo.tool.FileTool;
import com.springboot.demo.tool.Global;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @GetMapping("/document/create")
    public Result create(@RequestParam("user_id") Integer user_id,
                         @RequestParam("group_id") Integer group_id,
                         @RequestParam("type") Integer type){

        if(user_id == null){
            return Result.error(400,"用户不存在");
        }

        Optional<User> optionalUse = userRepository.findById(user_id);
        if(!optionalUse.isPresent()){
            return Result.error(400,"用户不存在");
        }

        //缺一判断团体存在的
        if(group_id == -1){
            group_id = null;
        }

        Date now = new Date(System.currentTimeMillis());
        System.out.println(now);
        Document document = new Document(0,user_id,group_id,now,
                now,false,false,"newName",0,null);
        document.setId((int) (System.currentTimeMillis()%2000000011));
        documentRepository.save(document);
        Authority_userKey authority_userKey=new Authority_userKey(user_id,document.getId());
        Authority_user authority_user=new Authority_user();
        authority_user.setAuthority_userKey(authority_userKey);
        authority_user.setCan_read(true);
        authority_user.setCan_edit(true);
        authority_user.setCan_comment(true);
        authority_user.setCan_delete(true);

        authorityRepository.save(authority_user);
        FileTool.writeFile(Global.DOCUMENT_PATH+document.getId()+".html","");
        return Result.success(document);
    }

    @GetMapping("/document/view")
    public Result view(@RequestParam("doc_id") Integer id){
        String filePath = Global.DOCUMENT_PATH + id.toString() + ".html";
        try{
            return Result.success(FileTool.readFile(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(400,"文章不存在");
        }
    }

    @GetMapping("/document/info")
    public Result info(@RequestParam("doc_id") Integer id){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(optionalDocument.isPresent()){
            return Result.success(fuck(optionalDocument.get()));
        }else{
            return Result.error(400,"文章不存在");
        }
    }

    @GetMapping("/document/edit/start")
    public Result edit_start(@RequestParam("doc_id") Integer id){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(!optionalDocument.isPresent()){
            return Result.error(400,"文章不存在");
        }
        else{
            if(optionalDocument.get().isIs_editting()){
                return Result.error(401,"文章正在被编辑");
            }
            else{
                optionalDocument.get().setIs_editting(true);
                documentRepository.save(optionalDocument.get());
                return Result.success();
            }
        }
    }

    @PostMapping("/document/edit/end")
    public Result edit_end(@RequestParam("doc_id") Integer id, @RequestBody String Data){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(!optionalDocument.isPresent()){
            return Result.error(400,"文章不存在");
        }
        else{
            Document document=optionalDocument.get();
            if (!document.isIs_editting()){
                return Result.error(400,"文章上传不成功");
            }
            else {
                document.setIs_editting(false);
                Date now = new Date(System.currentTimeMillis());
                document.setEdit_times(document.getEdit_times()+1);
                document.setLast_edit_time(now);
                documentRepository.save(document);
                FileTool.writeFile(Global.DOCUMENT_PATH+document.getId()+".html",Data);
                return Result.success();
            }

        }

    }

    @GetMapping("/document/delete")
    public Result delete(@RequestParam("doc_id") Integer doc_id,@RequestParam("user_id") Integer user_id){
        Optional<Document> optionalDocument = documentRepository.findById(doc_id);
        Document document=optionalDocument.get();
        if(!document.getCreator_id().equals(user_id)){
            return Result.error(400,"用户不具备删除权限");
        }
        document.setIs_deleted(true);
        documentRepository.save(document);
        return Result.success();
    }

    @GetMapping("/document/recycle")
    public Result recycle(@RequestParam("user_id") Integer user_id){
        Document tmpDocu=new Document();
        tmpDocu.setIs_deleted(true);
        tmpDocu.setCreator_id(user_id);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("creator_id", ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher("is_deleted", ExampleMatcher.GenericPropertyMatcher::exact)
                .withIgnorePaths("id");
        Example<Document> example = Example.of(tmpDocu,matcher);
        List<Document> myDocus=documentRepository.findAll(example);
        if(myDocus==null){
            return Result.error(400,"回收站为空");
        }
        return Result.success(myDocus);
    }

    @GetMapping("/document/recover")
    public Result recover(@RequestParam("doc_id") Integer id){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(!optionalDocument.isPresent()){
            return Result.error(400,"文章不存在");
        }
        else{
            if(!optionalDocument.get().isIs_deleted()){
                return Result.error(400,"文件可恢复");
            }
            optionalDocument.get().setIs_deleted(false);
            documentRepository.save(optionalDocument.get());
            return Result.success();
        }
    }

    public Document fuck(Document document){
        User user = userRepository.findById(document.getCreator_id()).orElse(null);
        if(user != null){
            document.setUsername(user.getName());
        }
        return document;
    }

}
