package Sword.Group.FirstTask.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import Sword.Group.FirstTask.userDetails.UserInfoUserDetailsService;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthFilter authFilter;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	// authentication
	public UserDetailsService userDetailsService() {
//        UserDetails admin = User.withUsername("Basant")
//                .password(encoder.encode("Pwd1"))
//                .roles("ADMIN")
//                .build();
//        UserDetails user = User.withUsername("John")
//                .password(encoder.encode("Pwd2"))
//                .roles("USER","ADMIN","HR")
//                .build();
//        return new InMemoryUserDetailsManager(admin, user);
		return new UserInfoUserDetailsService();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
//		return http.csrf().disable().authorizeHttpRequests().requestMatchers("/products/new", "/products/authenticate")
//				.permitAll().and().authorizeHttpRequests().requestMatchers("/products/**").authenticated().and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//				.authenticationProvider(authenticationProvider())
//				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
//		return http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//				.authenticationProvider(authenticationProvider())
//				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class).build();
		return http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				.and()
				.csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
				.build();

		// Disabled CsrfFilter, SecurityContextPersistenceFilter,
		// Disabled UsernamePasswordAuthenticationFilter.
	}

	// The actual metthod that does the authentication
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	// It manages the authentication and sends it to the AuthenticationProvider
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
