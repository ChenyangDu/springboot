package com.springboot.demo.entity;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Favorite {
    @EmbeddedId
    private FavorityKey favorityKey;
}
