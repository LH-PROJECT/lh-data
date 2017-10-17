package com.unitedratings.lhcrm.annotation;

import java.lang.annotation.*;

/**
 * 标识注解
 * 用于标注controller中的方法是否需要登录才能访问.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoNeedCheckLogin {
}
