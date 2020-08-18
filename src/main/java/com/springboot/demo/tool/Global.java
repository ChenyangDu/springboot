package com.springboot.demo.tool;

import java.util.Calendar;
import java.util.Date;

public class Global {
    //public final static String ROOT_PATH = "E:/Projects/small_software/";
    public final static String ROOT_PATH = "/root/springboot/resource/";
    public final static String DOC_IMG_PATH = ROOT_PATH + "docImg/";
    public final static String DOCUMENT_PATH = ROOT_PATH + "document/";
    public final static String DOCUMENT_MODEL_PATH = DOCUMENT_PATH + "model/";
    public final static String AVATAR_PATH = ROOT_PATH + "avatar/";
    public final static String SYSTEM_PATH = ROOT_PATH + "system/";
    public final static String SPECIAL_DOCUMENT_PATH = ROOT_PATH + "special/";
    public final static String INTRODUCTION_PATH = SPECIAL_DOCUMENT_PATH + "introduction/";

    public static Date nowTime(){
        Date now = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR,8);
        now = calendar.getTime();
        return now;
    }

}
