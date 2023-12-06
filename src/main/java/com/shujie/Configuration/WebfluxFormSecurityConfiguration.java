package com.shujie.Configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.shujie.CustomLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebfluxFormSecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WebfluxFormSecurityConfiguration.class);
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public ReactiveAuthenticationManager authenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }
    
    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    	logger.info("This is an security message.");
        http
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers(HttpMethod.GET, "/verify_email/**", "/reset_password/**","/v3/**", "/swagger-ui.html/**", "/webjars/**").permitAll()
                                .pathMatchers(HttpMethod.POST, "/signup", "/forgot_password", "/reset_password/**").permitAll()
//                                .pathMatchers(HttpMethod.PUT, "/api", "/api/**").permitAll()
//                                .pathMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                                .anyExchange().authenticated())
                .csrf(csrf -> csrf.disable())
                .httpBasic(withDefaults())
                .formLogin(formLogin ->
                formLogin
                        .loginPage("/signin")
        )
                .logout()
                .logoutUrl("/signout")
                .logoutSuccessHandler(new CustomLogoutSuccessHandler());
               
        return http.build();
    }
    
}

