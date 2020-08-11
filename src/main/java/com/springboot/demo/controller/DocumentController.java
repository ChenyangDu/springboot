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
import java.util.Date;
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
                now,false,false,"newName");
        document.setId((int) (System.currentTimeMillis()%2000000011));
        documentRepository.save(document);

        FileTool.writeFile(Global.DOCUMENT_PATH+document.getId()+".html","");
        return Result.success();
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
            return Result.success(optionalDocument.get());
        }else{
            return Result.error(400,"文章不存在");
        }
    }

}
