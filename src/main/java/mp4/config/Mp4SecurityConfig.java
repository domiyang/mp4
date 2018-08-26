package mp4.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import mp4.Mp4Helper;

/**
 * The security configuration class.
 * 
 * @author domi
 *
 */
@EnableWebSecurity
public class Mp4SecurityConfig extends WebSecurityConfigurerAdapter {
	private final static Logger log = LoggerFactory.getLogger(Mp4SecurityConfig.class);

	@Autowired
	AuthenticationSuccessHandler successLoginHandler;

	@Bean
	public AuthenticationSuccessHandler getSuccessLoginHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
					HttpServletResponse httpServletResponse, Authentication authentication)
					throws IOException, ServletException {
				RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
				redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
				log.info("login success handled.");

			}
		};
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(Mp4Helper.getAuthUser()).password(Mp4Helper.getAuthPass()).roles("mp4");
		log.info("set auth data.");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/js/**").permitAll().antMatchers("/css/**").permitAll().anyRequest()
				.authenticated().and().formLogin().loginPage("/login").permitAll().successHandler(successLoginHandler);
		log.info("set the security");
	}

}
