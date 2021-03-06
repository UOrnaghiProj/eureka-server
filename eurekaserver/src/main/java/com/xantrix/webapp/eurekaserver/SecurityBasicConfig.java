package com.xantrix.webapp.eurekaserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityBasicConfig extends WebSecurityConfigurerAdapter{
	
	@Value("${sicurezza.userpwd}")
	String user_pwd;
	
	@Value("${sicurezza.adminpwd}")
	String admin_pwd;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser("user")
			.password(new BCryptPasswordEncoder().encode(user_pwd))
			.roles("USER")
			.and()
			.withUser("admin")
			.password(new BCryptPasswordEncoder().encode(admin_pwd))
			.roles("USER","ACTUATOR");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.httpBasic()
			.and()
			.authorizeRequests()
			.antMatchers("/actuator/**").hasAnyAuthority("ROLE_ACTUATOR")
			.antMatchers("/**").hasAuthority("ROLE_USER");
		
	}
	
}
