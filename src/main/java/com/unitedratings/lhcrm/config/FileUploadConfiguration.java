package com.unitedratings.lhcrm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class FileUploadConfiguration {

    @Autowired
    private FileConfig fileConfig;

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        String location = fileConfig.getUploadPath()+File.separator+"temp";
        MultipartConfigFactory configFactory = new MultipartConfigFactory();
        File tempFileLocation = new File(location);
        if(!tempFileLocation.exists()){
            tempFileLocation.mkdirs();
        }
        configFactory.setLocation(location);
        return configFactory.createMultipartConfig();
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",buildConfig());
        return new CorsFilter(source);
    }

    private CorsConfiguration buildConfig(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        return corsConfiguration;
    }

}
