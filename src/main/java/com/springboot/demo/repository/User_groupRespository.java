package com.springboot.demo.repository;

import com.springboot.demo.entity.User_group_relation;
import com.springboot.demo.entity.User_group_relationKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface User_groupRespository extends JpaRepository<User_group_relation, User_group_relationKey> {
}
