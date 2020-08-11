package com.springboot.demo.repository;

import com.springboot.demo.entity.Favorite;
import com.springboot.demo.entity.FavorityKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, FavorityKey> {
}
