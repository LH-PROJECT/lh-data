package com.unitedratings.lhcrm.utils;

import org.springframework.context.ApplicationContext;

/**
 * @author wangyongxin
 */
public class SpringApplicationContextUtil {

    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        SpringApplicationContextUtil.context = context;
    }
}
