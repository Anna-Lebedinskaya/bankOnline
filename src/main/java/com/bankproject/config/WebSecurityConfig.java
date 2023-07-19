package com.bankproject.config;

import com.bankproject.service.userdetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.bankproject.constants.SecurityConstants.*;
import static com.bankproject.constants.UserRolesConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    CustomUserDetailsService customUserDetailsService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().disable() //убираем защиту, т.к. для проекта просто не надо
                .csrf().disable() //убираем защиту, т.к. для проекта просто не надо
                //настройка http-запросов - кому / куда, можно / нельзя
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(RESOURCES_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(USERS_WHITE_LIST.toArray(String[]::new)).permitAll()
                        .requestMatchers(BANK_ACCOUNTS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(TYPES_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(ADMIN)
                        .requestMatchers(TRANSACTIONS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(MANAGER, ADMIN)
                        .requestMatchers(USERS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(MANAGER, USER, ADMIN)
//                        .requestMatchers(USERS_PERMISSION_LIST.toArray(String[]::new)).hasAnyRole(MANAGER, USER)
                        .anyRequest().authenticated()
                )
                //настройки для входа в систему
                .formLogin((form) -> form
                        .loginPage("/login")
                        //перенаправление после успешного логина
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                );
        return httpSecurity.build();
    }

//    AuthenticationManagerBuilder //оперирует информацией о пользователе user details, кот-ю хранит в своем контексте

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }
}
