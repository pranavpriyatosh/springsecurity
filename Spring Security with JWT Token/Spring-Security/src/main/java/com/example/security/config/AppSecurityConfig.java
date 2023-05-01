package com.example.security.config;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.example.security.filter.JwtRequestFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;


@Configuration
@EnableWebSecurity                                                    //Enable the web security for application
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	
	  @Autowired
	 private JwtRequestFilter jwtRequestFilter;
	
	 @Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public AuthenticationProvider authProvider() {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(new BCryptPasswordEncoder());     //BCrypt is password-hashing function  
		return provider;
	}

	
	//for customize security
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			/*.csrf().disable()  
			.authorizeRequests().antMatchers("/login").permitAll()  //use to apply authorization to one or more paths specified in antMatchers()
			.anyRequest().authenticated()
			.and()
			.formLogin()                           //spring security provides support for username & password being provided through HTML form
			
			.loginPage("/login").permitAll()       //when authentication is required redirect to login page
			.and()
			.logout().invalidateHttpSession(true)  //allows session to be set up so that it's not invalidated when logout occurs
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/logout-success").permitAll();    //After successful logout redirect user to specified page
	
			//.httpBasic();
			*/
		.csrf().disable()  
		.authorizeRequests().antMatchers("/authenticate").permitAll()  //use to apply authorization to one or more paths specified in antMatchers()
		.anyRequest().authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	
	}
	
		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}
		
	
}
