package com.springboot.demo.controller;

import com.springboot.demo.entity.Document;
import com.springboot.demo.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/document")
    public Result document(@RequestParam("key") String key){
        List<Document> list = documentRepository.findAll();
        List<Document> result = new ArrayList<>();
        for(Document document : list){
            if(document.getName() != null && document.getName().contains(key)){
                result.add(document);
            }
        }
        return Result.success(result);
    }

}
