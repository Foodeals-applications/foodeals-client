package net.foodeals.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Value("${upload.directory}")
	private String uploadDir;

	@Value("${user.dir}")
	private String usrDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/photos/**") // URL prefix
		 .addResourceLocations("file:" + usrDir.replace("\\", "/") + "/" + uploadDir + "/");
	}
}