package com.avereon.www;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SiteConfiguration implements WebMvcConfigurer {

	@Override
	public void addViewControllers( ViewControllerRegistry registry ) {
		// A @Controller must be defined for this configuration to work
		registry.addViewController( "/{spring:\\w+}" ).setViewName( "forward:/index.html" );
		registry.addViewController( "/**/{spring:\\w+}" ).setViewName( "forward:/index.html" );
		registry.addViewController( "/{spring:\\w+}/**{spring:?!(\\.js|\\.css)$}" ).setViewName( "forward:/index.html" );
	}

}
