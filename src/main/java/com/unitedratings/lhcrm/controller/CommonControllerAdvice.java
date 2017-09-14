package com.unitedratings.lhcrm.controller;

import com.unitedratings.lhcrm.web.model.ResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CommonControllerAdvice {
    private static Logger LOGGER = LoggerFactory.getLogger(CommonControllerAdvice.class);

    /**
     * 公共异常处理
     * @param e 异常
     * @return
     */
    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public ResponseData<String> handlerException(HttpServletRequest request, Throwable e) {
        ResponseData<String> responseData;
        LOGGER.error("发送异常，请求链接是:" + request.getServletPath(), e);
        if (e instanceof Exception) {
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, e.getMessage());
        } else {
            // 非 exception 异常
            responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_FAILURE, "严重错误");
        }
        return responseData;
    }

}
