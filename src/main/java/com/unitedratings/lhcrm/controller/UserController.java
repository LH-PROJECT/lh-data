package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.annotation.NoNeedCheckLogin;
import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.entity.User;
import com.unitedratings.lhcrm.service.interfaces.UserServiceSV;
import com.unitedratings.lhcrm.web.model.ResponseData;
import com.unitedratings.lhcrm.web.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceSV userService;

    @NoNeedCheckLogin
    @PostMapping("/login")
    public ResponseData<UserModel> login(@RequestBody User user, HttpServletRequest request){
        if(!StringUtils.isEmpty(user.getUsername())&&!StringUtils.isEmpty(user.getPassword())){
            User u = userService.loginUser(user);
            if(u!=null){
                request.getSession().setAttribute(Constant.SESSION_USER_KEY,u);
                UserModel result = new UserModel();
                BeanUtils.copyProperties(u,result);
                return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"登陆成功",result);
            }
        }
        return new ResponseData<>(ResponseData.AJAX_STATUS_FAILURE,"登录失败，用户名或密码不正确");
    }

    @GetMapping("/logout")
    public ResponseData<String> logout(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(Constant.SESSION_USER_KEY);
        if(user!=null){
            request.getSession().setAttribute(Constant.SESSION_USER_KEY,null);
        }
        return new ResponseData<>(ResponseData.AJAX_STATUS_SUCCESS,"退出成功");
    }
}
