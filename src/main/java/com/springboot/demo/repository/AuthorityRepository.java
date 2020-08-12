package com.springboot.demo.repository;

import com.springboot.demo.entity.Authority_user;
import com.springboot.demo.entity.Authority_userKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority_user, Authority_userKey> {
}
