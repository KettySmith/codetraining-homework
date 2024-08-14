//package com.example.user.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
///**
// * @Author plj
// * @Date 2022/8/5 16:40
// */
//@Configuration
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        // 忽略对以下地址的安全校验
//        web.ignoring().antMatchers(
//                "/login",
//                "/logout",
//                "/css/**",
//                "/js/**",
//                "/index.html",
//                "favicon.ico",
//                "/doc.html",
//                "/webjars/**",
//                "/swagger-resources/**",
//                "/v2/api-docs/**",
//                "/swagger-ui.html",
//                "/swagger-ui/**",
//                "/configuration/ui",
//                "/configuration/security"
//        );
//    }
//
//}