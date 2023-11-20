package com.shujie;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import static org.springframework.security.config.Customizer.withDefaults;
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
////     @Bean
////        public MapReactiveUserDetailsService userDetailsService() {
////            UserDetails user = User
////              .withUsername("user")
////              .password("password")
////              .roles("USER")
////              .build();
////
////            UserDetails admin = User
////              .withUsername("admin")
////              .password("password")
////              .roles("ADMIN")
////              .build();
////
////            return new MapReactiveUserDetailsService(user, admin);
////        }
//
//      @Bean
//      public PasswordEncoder passwordEncoder() {
//          return new BCryptPasswordEncoder();
//      }
//
//
//  @Bean
//  public MapReactiveUserDetailsService userDetailsService() {
//     // @formatter:off
//     UserDetails user = User.withDefaultPasswordEncoder()
//        .username("user")
//        .password("password")
//        .roles("USER")
//        .build();
//     // @formatter:on
//     return new MapReactiveUserDetailsService(user);
//  }
//
////    @Bean
////    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
////       // @formatter:off
////       http
////          .authorizeExchange((authorize) -> authorize
////             .pathMatchers("/login").permitAll()
////             .anyExchange().authenticated()
////          )
////          .httpBasic(withDefaults())
////          .formLogin((form) -> form
////             .loginPage("/login")
////          );
////       // @formatter:on
////       return http.build();
////    }
//}
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebfluxFormSecurityConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(WebfluxFormSecurityConfiguration.class);

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

//  @Bean
//  public SecurityWebFilterChain SecurityFilterChain(ServerHttpSecurity http) {
//     logger.info("This is an web security message.");
//     http
//        .authorizeExchange((authorize) -> authorize
//              .requestMatchers("/api/**").permitAll()
//            .anyExchange().denyAll()
//        )
//        .csrf(csrf -> csrf.disable());
//     return http.build();
//  }

//  import static org.springframework.security.authorization.AuthorityReactiveAuthorizationManager.hasRole;
    // ...
//  @Bean
//  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
//     logger.info("This is an security message.");
//     http
//        .authorizeExchange((authorize) -> authorize
//           .pathMatchers("/api/**").permitAll())
//           .csrf(csrf -> csrf.disable());
//     return http.build();
//  }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        /*
        httpSecurity.authorizeHttpRequests(configurer ->
                configurer
                    .requestMatchers(HttpMethod.GET, "/employees")
                        .hasRole("EMPLOYEE")
                    .requestMatchers(HttpMethod.GET, "/employees/**")
                        .hasRole("EMPLOYEE")
                    .requestMatchers(HttpMethod.POST, "/employees")
                        .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.PUT, "/employees")
                        .hasRole("MANAGER")
                    .requestMatchers(HttpMethod.DELETE, "/employees/**")
                        .hasRole("ADMIN")
        );
         */

        httpSecurity.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/employees").permitAll()
                        .requestMatchers(HttpMethod.GET, "/employees/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/employees").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/employees").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/employees/**").permitAll()
        );


        //USE HTTP basic config
        httpSecurity.httpBasic(Customizer.withDefaults());

        //disable Cross Site Request Forgery(CSRF)
        //not required by stateless REST APIs that use POST, PUT . DELETE, PATCH
        httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }

//  @Bean
//  public SecurityFilterChain springWebFilterChain(HttpSecurity http) throws Exception {
//     http
//        .authorizeHttpRequests((requests) -> requests
//           .requestMatchers("/", "/test").permitAll()
//           .anyRequest().authenticated()
//        )
//        .formLogin((form) -> form
//           .loginPage("/login")
//           .permitAll()
//        )
//        .logout((logout) -> logout.permitAll());
//
//     return http.build();
//  }

//  @Bean
//  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//     // @formatter:off
//     http
//        .authorizeExchange((authorize) -> authorize
//           .pathMatchers("/login").permitAll()
//           .anyExchange().authenticated()
//        )
//        .httpBasic(withDefaults())
//        .formLogin((form) -> form
//           .loginPage("/login")
//        );
//     // @formatter:on
//     return http.build();
//  }
//
//   @Bean
//      public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//          http
//          .authorizeExchange((authorize) -> authorize
//                  .pathMatchers("/users").permitAll()  // Allow unauthenticated access to /users/**
//                  .anyExchange().denyAll()
//              )
//              .httpBasic(withDefaults())
//              .formLogin(withDefaults()); // Use default form login
//
//          return http.build();
//      }

//@Configuration
//@EnableWebSecurity
//public class WebfluxFormSecurityConfiguration {
//
//  @Bean
//  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//     http
//        .authorizeHttpRequests((authorize) -> authorize
//           .requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
//           .anyRequest().authenticated()
//        )
//        .httpBasic(Customizer.withDefaults())
//        .formLogin(Customizer.withDefaults());
//
//     return http.build();
//  }
//
//  @Bean
//  public AuthenticationManager authenticationManager(
//        UserDetailsService userDetailsService,
//        PasswordEncoder passwordEncoder) {
//     DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//     authenticationProvider.setUserDetailsService(userDetailsService);
//     authenticationProvider.setPasswordEncoder(passwordEncoder);
//
//     ProviderManager providerManager = new ProviderManager(authenticationProvider);
//     providerManager.setEraseCredentialsAfterAuthentication(false);
//
//     return providerManager;
//  }
//
//  @Bean
//  public UserDetailsService userDetailsService() {
//     UserDetails userDetails = User.withDefaultPasswordEncoder()
//        .username("user")
//        .password("password")
//        .roles("USER")
//        .build();
//
//     return new InMemoryUserDetailsManager(userDetails);
//  }
//
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//     return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//  }
//
}
