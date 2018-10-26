package mp4.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

import mp4.Mp4Helper;

/**
 * The web server configuration class.
 * 
 * @author domi
 *
 */
@Configuration
public class Mp4WebServerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
	private final static Logger log = LoggerFactory.getLogger(Mp4WebServerConfig.class);

	@Autowired
	Mp4Helper mp4Helper;
	
	@Override
	public void customize(TomcatServletWebServerFactory factory) {
		factory.setPort(mp4Helper.getServerPort());
		log.info("set the server port.");
	}

}
