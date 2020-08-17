package com.springboot.demo.controller;

import com.springboot.demo.tool.Global;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/image")
public class ImageController {
    //头像上传与下载
    @PostMapping("/avatar/upload")
    public Result upload(MultipartFile file,@RequestParam("user_id") int user_id) throws IOException{
        File dest = new File(Global.AVATAR_PATH+user_id+".jpg");
        file.transferTo(dest);
        return Result.success();
    }
    @RequestMapping(value = "/avatar/show",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@RequestParam("user_id")int user_id) throws IOException {
        try{
            File file = new File(Global.AVATAR_PATH+user_id+".jpg");
            FileInputStream inputStream = new FileInputStream(file);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, inputStream.available());
            return bytes;
        } catch (Exception e) {
        }
        return null;
    }
    // 文档里面的图片上传与下载
    @PostMapping("/docimg/upload")
    public String upload(MultipartFile file) throws IOException{
        int id = ((int) (System.currentTimeMillis()%2000000011));
        File dest = new File(Global.DOC_IMG_PATH+id+".jpg");
        file.transferTo(dest);
        return "{\"location\":\"http://39.101.200.9:8081/image/docimg/show?id="+id+"\"}";
    }
    @RequestMapping(value = "/docimg/show",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getdocImage(@RequestParam("id")int id) throws IOException {
        File file = new File(Global.DOC_IMG_PATH+id+".jpg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }


    //系统图片
    @RequestMapping(value = "/system",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getSystemImage(@RequestParam("id")String id) throws IOException {
        File file = new File(Global.SYSTEM_PATH+id+".jpg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
    //模板图片
    @RequestMapping(value = "/model",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getModelImage(@RequestParam("id")String id) throws IOException {
        File file = new File(Global.DOCUMENT_MODEL_PATH+id+".jpg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
}
