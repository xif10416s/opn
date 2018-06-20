package com.fxi.opn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.filter.CharacterEncodingFilter;

@PropertySource("file:./application.properties")
@SpringBootApplication
public class OpnApplication {
	private static Logger logger = LoggerFactory.getLogger(OpnApplication.class);
	public static void main(String[] args) {
		logger.info("OpnApplication Starting Spring Boot application..");
		SpringApplication.run(OpnApplication.class, args);
	}

	@Bean
	@ConditionalOnMissingBean(CharacterEncodingFilter.class)
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding("UTF-8");
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
	}
}
