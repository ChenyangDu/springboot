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
        System.out.println("hi");
        List<String> list = Arrays.asList( Global.DOCUMENT_PATH,Global.AVATAR_PATH,Global.SYSTEM_PATH);
        for(String path : list){
            File file =  new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
        }
    }

}
