package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class User {

    @Id
    private int id;
    private String name;
    private String password;
    private String phone;
    private String wechat;
    private String qq;
}
