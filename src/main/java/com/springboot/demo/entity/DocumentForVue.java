package com.springboot.demo.entity;

import com.springboot.demo.controller.UserController;
import com.springboot.demo.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@Data
public class DocumentForVue {
    private Integer id;
    private Integer creator_id;
    private Integer group_id;
    private Date create_time;
    private Date last_edit_time;
    private boolean is_deleted;
    private boolean is_editting;
    private String name;
    private Integer edit_times;

    private String username;

    public DocumentForVue(Document d){
        id = d.getId();
        creator_id = d.getCreator_id();
        group_id = d.getGroup_id();
        create_time = d.getCreate_time();
        last_edit_time = d.getLast_edit_time();
        is_deleted = d.isIs_deleted();
        is_deleted = is_editting;
        name = d.getName();
        edit_times = d.getEdit_times();
        username = new UserController().getById(creator_id).getName();
    }
}
