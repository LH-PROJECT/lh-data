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
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceSVTest {

    private static final char[] PASSWORD_CHAR=  {'1','2','3','4','5','6','7','8','9','0',
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z','!','#','$','%','&','-'};

    private static final int DEFAULT_PASSWORD_SIZE = 8;

    @Autowired
    private UserServiceSV userService;

    @Test
    public void userAdd(){
        User user = new User();
        user.setUsername("admin");
        user.setDisplayName("admin");
        user.setPassword(Md5Encoder.encodePassword(Constant.DEFAULT_PASSWORD));
        user.setCreateTime(new Date());
        user.setValid(true);
        userService.saveUser(user);
    }

    @Test
    public void generateRandomPasswordForUser(){
        List<User> userList = userService.getAllUser();
        Date date = new Date();
        for(User user:userList){
            if(!"admin".equals(user.getUsername())){
                String password = getRandomPassword();
                user.setInitPassword(password);
                user.setPassword(Md5Encoder.encodePassword(password));
                user.setCreateTime(date);
                userService.saveUser(user);
            }
        }
    }

    private String getRandomPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for(int i=0;i<DEFAULT_PASSWORD_SIZE;i++){
            password.append(PASSWORD_CHAR[random.nextInt(PASSWORD_CHAR.length)]);
        }
        return password.toString();
    }


    @Test
    public void test(){
        for (int i=0;i<10;i++){
            System.out.println(getRandomPassword());
        }
    }
}