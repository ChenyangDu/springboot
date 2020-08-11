package com.springboot.demo.controller;

import com.springboot.demo.entity.Document;
import com.springboot.demo.entity.User;
import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.UserRepository;
import com.springboot.demo.tool.FileTool;
import com.springboot.demo.tool.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

@CrossOrigin
@RestController
public class DocumentController {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        String ROOTPTATH = "E:\\Projects\\small_software";
        try {
            String str = FileTool.readFile(ROOTPTATH + "\\1.txt");
            System.out.println(str);
            FileTool.writeFile(ROOTPTATH+"\\2.html",str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/document/create")
    public Result create(@RequestParam("user_id") Integer user_id,@RequestParam("group_id") Integer group_id){
        if(user_id != null){
            return Result.error(400,"用户不存在");
        }

        Optional<User> optionalUse = userRepository.findById(user_id);
        if(!optionalUse.isPresent()){
            return Result.error(400,"用户不存在");
        }

        //缺一判断团体存在的

        Date now = new Date(System.currentTimeMillis());
        Document document = new Document(null,user_id,group_id,now,
                now,false,false,"newName");
        documentRepository.save(document);
        return Result.success();
    }

    @GetMapping("/docement/view")
    public Result view(@RequestParam("id") Integer id){
        String filePath = Global.DOCUMENT_PATH + id.toString() + ".html";
        try{
            return Result.success(FileTool.readFile(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(400,"文章不存在");
        }
    }

    @GetMapping("/document/info")
    public Result info(@RequestParam("id") Integer id){
        Optional<Document> optionalDocument = documentRepository.findById(id);
        if(optionalDocument.isPresent()){
            return Result.success(optionalDocument.get());
        }else{
            return Result.error(400,"文章不存在");
        }
    }

}
