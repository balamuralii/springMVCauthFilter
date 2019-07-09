package com.tutorialspoint;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan

public class SpringConfig extends WebMvcConfigurationSupport{

   

    @Bean

    public ViewResolver viewResolver() {
    	System.out.println(" java config");

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);

        viewResolver.setPrefix("/WEB-INF/jsp/");

        viewResolver.setSuffix(".jsp");

        return viewResolver;

    }

    @Override

    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

        configurer.enable();

    }
    
    @Bean(name="auth")
    public AuthTokenFilter authFilter() {
    	return new AuthTokenFilter();
    }

}
