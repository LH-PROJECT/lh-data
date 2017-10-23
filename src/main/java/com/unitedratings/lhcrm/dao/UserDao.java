package com.unitedratings.lhcrm.dao;

import com.unitedratings.lhcrm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangyongxin
 */
public interface UserDao extends JpaRepository<User,Integer>{
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 根据用户认证token查询用户信息
     * @param token 认证授权token
     * @return
     */
    User findByAccessToken(String token);
}
