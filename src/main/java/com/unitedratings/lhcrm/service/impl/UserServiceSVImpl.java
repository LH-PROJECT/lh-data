package com.unitedratings.lhcrm.service.impl;

import com.unitedratings.lhcrm.dao.UserDao;
import com.unitedratings.lhcrm.entity.User;
import com.unitedratings.lhcrm.service.interfaces.UserServiceSV;
import com.unitedratings.lhcrm.utils.Md5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceSVImpl implements UserServiceSV {

    @Autowired
    private UserDao userDao;

    @Override
    public User loginUser(User user) {
        User u = userDao.findByUsername(user.getUsername());
        if(u!=null&&u.getPassword().equalsIgnoreCase(user.getPassword())){
            final long date = System.currentTimeMillis();
            u.setLastLoginTime(new Date(date));
            u.setAccessToken(Md5Encoder.encodePassword(u.getUsername()+date));
            return userDao.save(u);
        }
        return null;
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User queryUserByToken(String token) {
        return userDao.findByAccessToken(token);
    }
}
