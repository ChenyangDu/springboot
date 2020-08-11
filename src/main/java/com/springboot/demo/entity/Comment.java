package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Comment {
    @Id
    private int user_id;
    @Id
    private int document_id;
    private String content;
}
