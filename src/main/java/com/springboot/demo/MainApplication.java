package com.springboot.demo;

import com.springboot.demo.tool.Global;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        init();
        SpringApplication.run(MainApplication.class, args);
    }

    private static void init(){
        List<String> list = Arrays.asList( Global.DOCUMENT_PATH,Global.AVATAR_PATH,Global.SYSTEM_PATH,
                Global.DOC_IMG_PATH,Global.DOCUMENT_MODEL_PATH,Global.INTRODUCTION_PATH);
        for(String path : list){
            File file =  new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
        }
    }

}
