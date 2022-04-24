package com.api.Comics.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.api.Comics.filters.JwtRequestFilter;
import com.api.Comics.service.ComicUserDetailsService;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
	@Autowired
	private ComicUserDetailsService userDetailsService;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
		    new AntPathRequestMatcher("/user/**"),
		    new AntPathRequestMatcher("/app/**")
		  );
	private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Disable authentication for login endpoint.
		/*http.csrf().disable()
			.authorizeRequests().antMatchers("/user/login").permitAll()
			.antMatchers("/user/createUser").permitAll()
			.anyRequest().authenticated().and().sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);*/
		http
			.cors().and()
	      .sessionManagement()
	      .sessionCreationPolicy(STATELESS)
	      .and()
	      .exceptionHandling()
	      // this entry point handles when you request a protected page and you are not yet
	      // authenticated
	      .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
	      .and()
	      //.authenticationProvider(provider)
	      .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
	      .authorizeRequests()
	      .requestMatchers(PROTECTED_URLS)
	      .authenticated()
	      .and()
	      .csrf().disable()
	      .formLogin().disable()
	      .httpBasic().disable()
	      .logout().disable();
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean
	AuthenticationEntryPoint forbiddenEntryPoint() {
		return new HttpStatusEntryPoint(FORBIDDEN);
	}
}
