package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Authority_user {
    @Id
    private int user_id;
    @Id
    private int document_id;
    private boolean can_read;
    private boolean can_comment;
    private boolean can_edit;
    private boolean can_delete;


}
