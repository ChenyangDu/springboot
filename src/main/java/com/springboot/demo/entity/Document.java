package com.springboot.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    private Integer id;
    private int creator_id;
    private int group_id;
    private Date create_time;
    private Date last_edit_time;
    private boolean is_deleted;
    private boolean is_editting;
    private String name;

}
