package mp4;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The helper class to provide some common helper methods.
 * 
 * @author domi
 *
 */
@Configuration
@EnableWebSecurity
public class Mp4Helper implements WebMvcConfigurer, WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
	private final static Logger log = LoggerFactory.getLogger(Mp4Helper.class);

	@Autowired
	private static Environment env = null;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/webapp/",
				"file:" + Mp4Helper.getMediaPath());
		log.info("set the static res mapping.");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:index.html");
		log.info("set the home page.");
	}

	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		factory.setPort(Mp4Helper.getServerPort());
		log.info("set the server port.");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(getAuthUser()).password(getAuthPass()).roles("mp4");
		log.info("set auth data.");
	}

	/**
	 * Get the env value from env bundle for key, use System.getPropery(...) if
	 * env is not available.
	 * 
	 * @param key
	 * @return
	 */
	public static String getEnvProperty(String key) {
		if (env != null) {
			return env.getProperty(key);
		}
		// use system properties when env not available
		return System.getProperty(key);
	}

	/**
	 * Get the basic auth user name from env, defaut to mp4 if not provided from
	 * java arguments via -Dmp4.user
	 * 
	 * @return
	 */
	public static String getAuthUser() {
		// default as mp4 if not provided
		String user = getEnvProperty("mp4.user");
		if (StringUtils.isEmpty(user)) {
			user = "mp4";
			log.info("loaded with default user=" + user);
		}

		return user;
	}

	/**
	 * Get the basic auth pass from env, defaut to mp4pass if not provided from
	 * java arguments via -Dmp4.pass
	 * 
	 * @return
	 */
	public static String getAuthPass() {
		// default as mp4pass if not provided
		String pass = getEnvProperty("mp4.pass");
		if (StringUtils.isEmpty(pass)) {
			pass = "mp4pass";
			log.info("loaded with default pass=" + pass);
		}

		return "{noop}" + pass;
	}

	/**
	 * Get the server listening port, default to 8080 if not provided from java
	 * arguments via -Dmp4.server.port
	 * 
	 * @return
	 */
	public static int getServerPort() {
		// default as 8080 if not provided
		String port = getEnvProperty("mp4.server.port");
		if (StringUtils.isEmpty(port)) {
			port = "8080";
			log.info("loaded with default port=" + port);
		}
		return Integer.parseInt(port);
	}

	/**
	 * Get the accepted media file types, default to mp4,mov,mp3,m4v if not
	 * provided from java arguments via D-mp4.media.types
	 * 
	 * @return
	 */
	public static List<String> getMediaTypeList() {
		// default as mp4,mov,mp3,m4v if not provided
		String mediaTypes = getEnvProperty("mp4.media.types");
		if (StringUtils.isEmpty(mediaTypes)) {
			mediaTypes = "mp4,mov,mp3,m4v";
			log.info("loaded with default videoSuffixList=" + mediaTypes);
		}
		return Arrays.asList(mediaTypes.split(","));
	}

	/**
	 * Get the media root directory to stream, mandatory java arguments via
	 * D-mp4.media.path
	 * 
	 * @return
	 */
	public static String getMediaPath() {
		String mediaPath = getEnvProperty("mp4.media.path");
		log.info("mediaPath=" + mediaPath);
		if (StringUtils.isEmpty(mediaPath)) {
			throw new IllegalArgumentException(
					"Please provide the java argument: -Dmp4.media.path, e.g.:java -Dmp4.media.path=/my/video -jar mp4.jar");
		}

		String fs = getEnvProperty("file.separator");
		log.info("fs=" + fs);
		if (!mediaPath.endsWith(fs)) {
			return mediaPath + fs;
		}

		return mediaPath;
	}

	/**
	 * check if targetStr end with list of lookupStr (case insensitive).
	 * 
	 * @param targetStr
	 * @param lookupStr
	 * @return
	 */
	public static boolean isStringEndWithListOfString(String targetStr, List<String> lookupStr) {
		for (String lookup : lookupStr) {
			if (targetStr.toLowerCase().endsWith(lookup.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Get list of files under dirPath with accepted file types in
	 * fileSuffixList.
	 * 
	 * @param dirPath
	 * @param fileTypeList
	 * @return
	 */
	public static List<File> getListOfFilesForDir(String dirPath, final List<String> fileTypeList) {
		log.info("try to laod files from dirPath=" + dirPath + ",for types=" + fileTypeList);
		List<File> list = new ArrayList<File>();
		File file = new File(dirPath);
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				list.addAll(getListOfFilesForDir(f.getAbsolutePath(), fileTypeList));
			} else {
				if (isStringEndWithListOfString(f.getName(), fileTypeList)) {
					list.add(f);
				} else {
					// skipping f
				}
			}
		}
		return list;
	}

}
