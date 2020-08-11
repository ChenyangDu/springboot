package com.springboot.demo;

import com.springboot.demo.tool.Global;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        init();
        SpringApplication.run(MainApplication.class, args);
    }

    private static void init(){
        System.out.println("hi");
        File file =  new File(Global.DOCUMENT_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        file = new File(Global.AVATAR_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
    }

}
