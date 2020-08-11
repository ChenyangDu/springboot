package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Group {
    @Id
    private Integer id;
    private String name;
    private Integer creator_id;
}
