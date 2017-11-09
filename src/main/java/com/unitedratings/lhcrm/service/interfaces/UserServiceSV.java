package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.User;

import java.util.List;

/**
 * @author wangyongxin
 */
public interface UserServiceSV {
    /**
     * 用户登录查询
     * @param user
     * @return
     */
    User loginUser(User user);

    /**
     * 保存用户
     * @param user
     * @return
     */
    User saveUser(User user);

    /**
     * 根据认证token查询用户信息
     * @param token
     * @return
     */
    User queryUserByToken(String token);

    /**
     * 查询所有用户
     * @return
     */
    List<User> getAllUser();

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    User getUserById(Integer userId);
}
