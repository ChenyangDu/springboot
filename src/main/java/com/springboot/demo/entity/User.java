package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    protected Integer id;
    protected String name;
    protected String password;
    protected String phone;
    protected String wechat;
    protected String qq;
    protected String email;
    protected String sign;
}
