package com.springboot.demo.entity;

import com.springboot.demo.repository.DocumentRepository;
import com.springboot.demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
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
    private Integer edit_times;

    @Transient
    private String username;
    @Transient
    private boolean star;

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public boolean isIs_editting() {
        return is_editting;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public void setIs_editting(boolean is_editting) {
        this.is_editting = is_editting;
    }
}
