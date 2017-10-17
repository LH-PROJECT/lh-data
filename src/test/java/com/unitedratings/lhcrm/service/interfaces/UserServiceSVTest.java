package com.unitedratings.lhcrm.service.interfaces;


import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.entity.User;
import com.unitedratings.lhcrm.utils.Md5Encoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceSVTest {

    @Autowired
    private UserServiceSV userService;

    @Test
    public void userAdd(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword(Md5Encoder.encodePassword(Constant.DEFAULT_PASSWORD));
        user.setCreateTime(new Date());
        user.setValid(true);
        userService.saveUser(user);
    }

}