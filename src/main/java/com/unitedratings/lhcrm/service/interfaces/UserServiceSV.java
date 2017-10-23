package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.User;

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
}
