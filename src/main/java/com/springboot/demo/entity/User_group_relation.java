package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class User_group_relation {
    @Id
    private int user_id;
    @Id
    private int group_id;
}
