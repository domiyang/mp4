package mp4.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mp4.Mp4Helper;

/**
 * The mvc configuration class.
 * 
 * @author domi
 *
 */
@Configuration
@EnableWebMvc
public class Mp4MvcConfig implements WebMvcConfigurer {
	private final static Logger log = LoggerFactory.getLogger(Mp4MvcConfig.class);

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/templates/", "classpath:/static/",
				"file:" + Mp4Helper.getMediaPath());
		log.info("set the static res mapping.");
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		log.info("set the home page.");

		registry.addViewController("/login").setViewName("login");
		log.info("set the login page.");
	}

}
