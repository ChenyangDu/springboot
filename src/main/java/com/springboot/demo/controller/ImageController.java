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
    @PostMapping("/avatar/upload")
    public Result upload(MultipartFile file,@RequestParam("user_id") int user_id) throws IOException{
        File dest = new File(Global.AVATAR_PATH+user_id+".jpg");
        file.transferTo(dest);
        return Result.success();
    }
    @RequestMapping(value = "/avatar/show",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@RequestParam("user_id")int user_id) throws IOException {
        File file = new File(Global.AVATAR_PATH+user_id+".jpg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
    @RequestMapping(value = "/system",produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getSystemImage(@RequestParam("id")String id) throws IOException {
        File file = new File(Global.SYSTEM_PATH+id+".jpg");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
    }
}
