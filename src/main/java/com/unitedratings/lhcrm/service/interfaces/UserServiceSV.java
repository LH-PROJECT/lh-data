package com.unitedratings.lhcrm.service.interfaces;

import com.unitedratings.lhcrm.entity.User;

public interface UserServiceSV {
    User loginUser(User user);

    User saveUser(User user);

    User queryUserByToken(String token);
}
