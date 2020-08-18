package com.springboot.demo.controller;

import com.springboot.demo.tool.FileTool;
import com.springboot.demo.tool.Global;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
public class SpecialDocumentController {

    @GetMapping("/introduction/view")
    public Result introductionView(@RequestParam("user_id") int user_id){
        try{
            String filepath = Global.INTRODUCTION_PATH+user_id+".html";
            return Result.success(FileTool.readFile(filepath));
        } catch (IOException e) {
            return Result.success("主人很懒，什么都没有留下");
        }
    }

    @PostMapping("/introduction/save")
    public Result introductionSave(@RequestParam("user_id")int user_id,
                                   @RequestBody String str){
        FileTool.writeFile(Global.INTRODUCTION_PATH+user_id+".html",str);
        return Result.success();
    }
}
