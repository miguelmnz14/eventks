package com.example.demo.security;

import com.example.demo.security.jwt.JwtRequestFilter;
import com.example.demo.security.jwt.UnauthorizedHandlerJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public RepositoryUserDetailsService userDetailService;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .securityMatcher("/api/**")
                .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PRIVATE ENDPOINTS
                        .requestMatchers(HttpMethod.POST,"/api/events").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/events/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/events/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/all/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/users/me").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/me").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/users/me").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/users/mytickets").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/signup").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/refresh").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/events/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/events/*/image").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/events/*/image").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/*/image").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/events/buy/*").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/*/comments/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/events/*/comments").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/events/*/comments").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/events").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/events/*").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/events/din").permitAll()


                        // PUBLIC ENDPOINTS
                        //  .anyRequest().permitAll()
                );

        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Token filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
                .authorizeHttpRequests(authorize -> authorize
                        // PUBLIC PAGES
                        // PRIVATE PAGES
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/events/new").hasRole("ADMIN")
                        .requestMatchers("/events/*/edit").hasRole("ADMIN")
                        .requestMatchers("/events/*/delete").hasRole("ADMIN")
                        .requestMatchers("/events/*/delete/*").hasRole("USER")
                        .requestMatchers("/private/**").hasRole("USER")
                        .requestMatchers("/tickets").hasRole("USER")
                        .requestMatchers("/buy/*").hasRole("USER")
                        .requestMatchers("/events/*/comments").hasRole("USER")
                        .requestMatchers("/**").permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }


}