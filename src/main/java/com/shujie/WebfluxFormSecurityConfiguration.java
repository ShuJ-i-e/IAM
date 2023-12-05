package com.shujie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class WebfluxFormSecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WebfluxFormSecurityConfiguration.class);
   
    
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
    
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
    
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
//    
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }
    
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
    

//    @Bean
//    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
//        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
//        authenticationWebFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
//        authenticationWebFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
//        return authenticationWebFilter;
//    }
//
//    @Bean
//    public ReactiveAuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return new ReactiveAuthenticationManagerAdapter(authenticationProvider);
//    }
    
//    @Bean
//    @Primary
//    public MapReactiveUserDetailsService mapReactiveUserDetailsService() {
//        UserDetails user = User.withUsername("user")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .build();
//
//        return new MapReactiveUserDetailsService(user);
//    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    	logger.info("This is an security message.");
        http
                .authorizeExchange(exchanges ->
                        exchanges
                                .pathMatchers(HttpMethod.GET, "/api","/api/**").permitAll()
                                .pathMatchers(HttpMethod.POST, "/api", "/register", "/api/**").permitAll()
                                .pathMatchers(HttpMethod.PUT, "/api", "/api/**").permitAll()
                                .pathMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                                .anyExchange().authenticated())
                .csrf(csrf -> csrf.disable())
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .logout()
                .logoutUrl("/signout")
                .logoutSuccessHandler(new CustomLogoutSuccessHandler());
               
        return http.build();
    }
    
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//    }
//    
    
}

