package com.unitedratings.lhcrm.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.concurrent.Callable;

/**
 * @author wangyongxin
 */
@RestController
public class IndexController {

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("index");
        return view;
    }

    @RequestMapping("/call")
    public Callable<String> call(){
        System.out.println(Thread.currentThread().getId());
        return ()->{
            Thread.sleep(10000);
            return "异步请求测试"+Thread.currentThread().getId();
        };
    }

}
