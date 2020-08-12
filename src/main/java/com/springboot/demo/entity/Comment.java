package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
public class Comment {
    @Id
    private Integer comment_id;
    private Integer user_id;
    private Integer document_id;
    private String content;
    private Date time;
}
