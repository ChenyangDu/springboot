package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class FavorityKey implements Serializable {

    @Column
    private Integer user_id;

    @Column
    private Integer document_id;

}
