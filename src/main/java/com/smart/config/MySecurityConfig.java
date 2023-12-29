package com.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class MySecurityConfig {
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService getDetailsService()
	{
		return  new UserDetailsServiceImpl();
	}
	
	
	@Bean
	public DaoAuthenticationProvider getAuthenticationProvider()
	{ 
		DaoAuthenticationProvider daoAuthenticationProvider = new
	  DaoAuthenticationProvider();
	  
	daoAuthenticationProvider.setUserDetailsService(getDetailsService());
	  daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); 
	  
	  
	  return daoAuthenticationProvider;
		
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http.csrf().disable()
		.authorizeHttpRequests().requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/user/**").hasRole("USER")
		.requestMatchers("/**").permitAll()
		.and().formLogin().loginPage("/signin")
		.loginProcessingUrl("/dologin")
		.defaultSuccessUrl("/user/index");
		
		
		
		return http.build();
		
	}
	
	
	
}
