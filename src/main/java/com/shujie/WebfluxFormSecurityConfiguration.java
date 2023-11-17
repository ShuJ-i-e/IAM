package com.shujie;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
///**
// * WebFlux configuration for custom log in page.
// *
// * @author Rob Winch
// * @since 5.0
// */
//@Configuration
//@EnableWebFluxSecurity
//public class WebfluxFormSecurityConfiguration{
//
////	 @Bean
////	    public MapReactiveUserDetailsService userDetailsService() {
////	        UserDetails user = User
////	          .withUsername("user")
////	          .password("password")
////	          .roles("USER")
////	          .build();
////
////	        UserDetails admin = User
////	          .withUsername("admin")
////	          .password("password")
////	          .roles("ADMIN")
////	          .build();
////
////	        return new MapReactiveUserDetailsService(user, admin);
////	    }
//	 
//	    @Bean
//	    public PasswordEncoder passwordEncoder() {
//	        return new BCryptPasswordEncoder();
//	    }
//
//	 
//	@Bean
//	public MapReactiveUserDetailsService userDetailsService() {
//		// @formatter:off
//		UserDetails user = User.withDefaultPasswordEncoder()
//			.username("user")
//			.password("password")
//			.roles("USER")
//			.build();
//		// @formatter:on
//		return new MapReactiveUserDetailsService(user);
//	}
//
////	@Bean
////	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
////		// @formatter:off
////		http
////			.authorizeExchange((authorize) -> authorize
////				.pathMatchers("/login").permitAll()
////				.anyExchange().authenticated()
////			)
////			.httpBasic(withDefaults())
////			.formLogin((form) -> form
////				.loginPage("/login")
////			);
////		// @formatter:on
////		return http.build();
////	}
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * WebFlux configuration for custom log in page.
 *
 * @author Rob Winch
 * @since 5.0
 */
@Configuration
@EnableWebFluxSecurity
public class WebfluxFormSecurityConfiguration {

	@Bean
	public MapReactiveUserDetailsService userDetailsService() {
		// @formatter:off
		UserDetails user = User.withDefaultPasswordEncoder()
			.username("user")
			.password("password")
			.roles("USER")
			.build();
		// @formatter:on
		return new MapReactiveUserDetailsService(user);
	}

	@Bean
	SecurityWebFilterChain SecurityFilterChain(ServerHttpSecurity http) {
		// @formatter:off
		http
			.authorizeExchange((authorize) -> authorize
				.pathMatchers("/login").permitAll()
				.anyExchange().authenticated()
			)
			.httpBasic(withDefaults())
			.formLogin((form) -> form
				.loginPage("/login")
			);
		// @formatter:on
		return http.build();
	}

}

