package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
public class Authority_user {
    @EmbeddedId
    private Authority_userKey authority_userKey;
    private boolean can_read;
    private boolean can_comment;
    private boolean can_edit;
    private boolean can_delete;
}
