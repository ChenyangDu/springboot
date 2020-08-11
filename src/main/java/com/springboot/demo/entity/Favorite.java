package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;


@Data
public class Favorite {
    @Id
    private int user_id;
    @Id
    private int document_id;
}
