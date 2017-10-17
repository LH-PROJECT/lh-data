package com.unitedratings.lhcrm.aop;

import com.unitedratings.lhcrm.annotation.NoNeedCheckLogin;
import com.unitedratings.lhcrm.constants.Constant;
import com.unitedratings.lhcrm.constants.TokenValidateResult;
import com.unitedratings.lhcrm.entity.User;
import com.unitedratings.lhcrm.service.interfaces.UserServiceSV;
import com.unitedratings.lhcrm.web.model.ResponseData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author wangyongxin
 */
@Component
@Aspect
public class AccessAuthAspect {

    @Autowired
    private UserServiceSV userService;

    @Around("execution(* com.unitedratings.lhcrm.controller..*.*(..))")
    public Object loginAspect(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if(isNeedLogin(proceedingJoinPoint)){
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = servletRequestAttributes.getRequest();
            TokenValidateResult tokenValidateResult = validateToken(request.getHeader(Constant.ACCESS_TOKEN));
            if (tokenValidateResult != TokenValidateResult.SUCCESS) {
                if (tokenValidateResult == TokenValidateResult.TOKEN_EXPIRED) {
                    return new ResponseData<String>(ResponseData.AJAX_STATUS_AUTH_EXPIRED, "认证失效，请重新认证");
                } else {
                    return new ResponseData<String>(ResponseData.AJAX_STATUS_NOT_AUTH, "你没有访问权限，请先进行认证");
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }

    private TokenValidateResult validateToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return TokenValidateResult.TOKEN_EMPTY;
        }

        //校验token
        User user = userService.queryUserByToken(token);
        if (user!=null) {
            return TokenValidateResult.SUCCESS;
        }else {
            return TokenValidateResult.TOKEN_NOT_EXIST;
        }
    }

    private boolean isNeedLogin(ProceedingJoinPoint proceedingJoinPoint) {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if(method!=null&&method.isAnnotationPresent(NoNeedCheckLogin.class)){
            return false;
        }
        return true;
    }

}
