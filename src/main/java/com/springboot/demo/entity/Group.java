package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "ggroup")
@Data
public class Group {
    @Id
    public Integer id;
    public String name;
    public Integer creator_id;
}
