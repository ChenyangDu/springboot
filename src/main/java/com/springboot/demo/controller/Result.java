package com.springboot.demo.controller;

import lombok.Data;

@Data
public class Result<T> {
    private int code; //返回状态码
    private String msg; //返回的message
    private T data;  //返回的数据

    private Result(T data) {
        this.code = 200;
        this.data = data;
    }

    private Result(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 失败的时候调用
     */
    public static <T> Result<T> error(int code, String msg) {
        return new Result<T>(code,msg);
    }

    /**
     *成功的时候调用
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }
    /**
     *成功的时候调用
     */
    public static <T> Result<T> success() {
        return new Result<T>(null);
    }
}