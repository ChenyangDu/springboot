package com.springboot.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Authority_user {
    @EmbeddedId
    private Authority_userKey authority_userKey;
    private boolean can_read;
    private boolean can_comment;
    private boolean can_edit;
    private boolean can_delete;
}
