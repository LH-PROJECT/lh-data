package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User,Integer>{
    User findByUsername(String username);

    User findByAccessToken(String token);
}
