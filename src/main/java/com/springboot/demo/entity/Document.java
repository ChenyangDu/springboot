package com.springboot.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
