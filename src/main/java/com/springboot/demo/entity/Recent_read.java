package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Recent_read {
    @Id
    private int user_id;
    private String document_id;

}
