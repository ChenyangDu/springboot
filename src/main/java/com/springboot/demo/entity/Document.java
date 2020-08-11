package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Data

public class Document {
    @Id
    private Integer id;
    private Integer creator_id;
    private Integer group_id;
    private Date create_time;
    private Date last_edit_time;
    private boolean is_deleted;
    private boolean is_editting;
    private String name;
}
