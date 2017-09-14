package com.unitedratings.lhcrm;

import com.unitedratings.lhcrm.utils.SpringApplicationContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LhcrmApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(LhcrmApplication.class, args);
		SpringApplicationContextUtil.setContext(applicationContext);
	}
}
