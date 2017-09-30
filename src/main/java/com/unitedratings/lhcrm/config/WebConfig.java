package com.unitedratings.lhcrm.config;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.unitedratings.lhcrm.interceptors.AnalysisResultInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /*@Autowired
    private JsonReturnHandler handler;*/

    @Autowired
    private AnalysisResultInterceptor analysisResultInterceptor;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/calDefaultRate").setViewName("index");
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .filters(new SimpleFilterProvider().addFilter("Matrix",SimpleBeanPropertyFilter.filterOutAllExcept("defaultRecordMatrix")));
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }


    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(10*60*1000);
        configurer.setTaskExecutor(taskExecutor());
        configurer.registerDeferredResultInterceptors(analysisResultInterceptor);
    }

    /*@Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
        returnValueHandlers.add(handler);
    }*/

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setQueueCapacity(100);
        return executor;
    }
}
