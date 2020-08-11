package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
public class Message {
    @Id
    private Integer id;
    private Integer receiver_id;
    private Integer sender_id;
    private Integer docu_id;
    private Integer group_id;
    private Integer message_type;
    private boolean have_read;

}
