package com.springboot.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    private Integer id;
    private Integer receiver_id;
    private Integer sender_id;
    private Integer docu_id;
    private Integer group_id;
    private Integer message_type;
    private boolean have_read;
    @Column(name = "comment_time")
    private Date time;

    @Transient
    private String receiver_name,sender_name,docu_name,group_name;

    public Message (Integer id,Integer receiver_id,Integer sender_id,Integer docu_id,
                    Integer group_id, Integer message_type,boolean have_read,Date time){
        this.id = id;
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
        this.docu_id = docu_id;
        this.group_id = group_id;
        this.message_type = message_type;
        this.have_read = have_read;
        this.time = time;
    }

}
