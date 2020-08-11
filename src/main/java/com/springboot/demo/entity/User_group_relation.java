package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class User_group_relation {
    @Id
    private Integer user_id;
    @Id
    private Integer group_id;
}
