package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Group {
    @Id
    private int id;
    private String name;
    private int creator_id;
}
