package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Message {
    @Id
    private int id;
    private int receiver_id;
    private int sender_id;
    private int docu_id;
    private int group_id;
    private int message_type;
    private boolean have_read;

}
