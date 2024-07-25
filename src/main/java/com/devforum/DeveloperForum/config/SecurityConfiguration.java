package com.devforum.DeveloperForum.config;

import com.devforum.DeveloperForum.security.JwtAuthenticationFilter;
import com.devforum.DeveloperForum.services.UserDetailsServiceImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserDetailsServiceImplementation userDetailsServiceImplementation;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter,
                                 UserDetailsServiceImplementation userDetailsServiceImplementation) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsServiceImplementation = userDetailsServiceImplementation;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/users").permitAll()
                            .requestMatchers("/auth/**").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(AbstractAuthenticationFilterConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsServiceImplementation);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(daoAuthenticationProvider());
    }

}
