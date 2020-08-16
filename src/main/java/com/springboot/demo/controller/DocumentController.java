package com.springboot.demo.controller;

import com.springboot.demo.entity.*;
import com.springboot.demo.repository.*;
import com.springboot.demo.tool.FileTool;
import com.springboot.demo.tool.Global;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@CrossOrigin
@RestController
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private Recent_readRepository recent_readRepository;

    @GetMapping("/document/create")
    public Result create(@RequestParam("user_id") Integer user_id,
                         @RequestParam("group_id") Integer group_id,
                         @RequestParam("name") String name,
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

        Date now = Global.nowTime();
        Document document = new Document(0,user_id,group_id,now,
                now,false,false,name,0,null);
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
    public Result view(@RequestParam("doc_id") Integer doc_id,@RequestParam("user_id") Integer user_id){
        String filePath = Global.DOCUMENT_PATH + doc_id.toString() + ".html";
        String document_list = "";
        if(user_id == null)return Result.error(400,"请登录");
        Recent_read recent_read = recent_readRepository.findById(user_id).orElse(null);
        if(recent_read != null){
            document_list = recent_read.getDocument_list();
            List<String> documents = new ArrayList<>(Arrays.asList(document_list.split(",")));
            documents.add(0,doc_id.toString());
            while(documents.size() > 10){
                documents.remove(10);
            }
            document_list = "";
            for(int i = 0 ;i < documents.size();i++){
                document_list += documents.get(i);
                if(i != documents.size()-1){
                    document_list += ",";
                }
            }
            recent_read.setDocument_list(document_list);
        }else{
            recent_read = new Recent_read();
            recent_read.setUser_id(user_id);
            recent_read.setDocument_list(doc_id.toString());
        }

        recent_readRepository.save(recent_read);

        try{
            return Result.success(FileTool.readFile(filePath));
        } catch (IOException e) {
            recent_readRepository.delete(recent_read);
            e.printStackTrace();
            return Result.error(400,"文章不存在");
        }
    }

    @GetMapping("/document/rename")
    public Result rename(@RequestParam("doc_id") Integer id,@RequestParam("name") String name){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        Document tmpDoc=optionalDocument.get();
        if(tmpDoc.isIs_editting()){
            return Result.error(400,"文章标题修改失败");
        }
        tmpDoc.setName(name);
        documentRepository.save(tmpDoc);
        return Result.success();

    }

    @GetMapping("/document/info")
    public Result info(@RequestParam("doc_id") Integer doc_id){
        Optional<Document> optionalDocument = documentRepository.findById(doc_id);
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
        for(Document document : myDocus){
            fuck(document);
        }
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

    @GetMapping("/document/remove")
    public Result remove(@RequestParam("doc_id") Integer id,@RequestParam("user_id") Integer user_id){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        Document tmpDoc=optionalDocument.get();
        if(!tmpDoc.getCreator_id().equals(user_id)){
            return Result.error(400,"用户不具备删除权限");
        }
        String filePath = Global.DOCUMENT_PATH + id.toString() + ".html";
        FileTool.deleteFile(filePath);
        documentRepository.delete(tmpDoc);
        return Result.success();
    }

    @GetMapping("/document/favorite")
    public Result favorite(@RequestParam("doc_id") int doc_id,
                           @RequestParam("user_id") int user_id,
                           @RequestParam("favorite")boolean is_favorite){

        Favorite favorite = new Favorite(new FavorityKey(user_id,doc_id));
        if(is_favorite){
            favoriteRepository.save(favorite);
        }else{
            favoriteRepository.delete(favorite);
        }
        return Result.success();
    }
    @GetMapping("/document/favoriteinfo")
    public Result favoriteinfo(@RequestParam("doc_id") Integer id,@RequestParam("user_id") Integer user_id){
        FavorityKey favorityKey=new FavorityKey();
        favorityKey.setDocument_id(id);
        favorityKey.setUser_id(user_id);
        Optional<Favorite> optionalFavorite=favoriteRepository.findById(favorityKey);
        if(!optionalFavorite.isPresent()){
            return Result.success(false);
        }
        return Result.success(true);
    }

    public Document fuck(Document document){
        User user = userRepository.findById(document.getCreator_id()).orElse(null);
        if(user != null){
            document.setUsername(user.getName());
        }
        return document;
    }

}
