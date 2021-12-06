package com.example.demo.common.config;

import com.example.demo.service.User1Service;
import com.example.demo.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    User1Service user1Service;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    CustomizeAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    CustomizeAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    CustomizeAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    CustomizeLogoutSuccessHandler customizeLogoutSuccessHandler;
    @Autowired
    CustomizeSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置认证方式
        auth.userDetailsService(userDetailsService());
    }

//    @Bean
//    HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//         任意请求允许无授权访问
//        http.authorizeRequests()
//                .antMatchers("/", "/home").permitAll().and().csrf().disable();
        http.cors().and().csrf().disable();
        http.authorizeRequests()
//                .antMatchers("/**").hasAuthority("ROLE_USER")
//                .antMatchers("/box/**").hasAuthority("ROLE_USER")
//                .antMatchers("/drawing/**").hasAuthority("ROLE_USER")
//                .antMatchers("/user/**").hasAuthority("ROLE_USER")
                .and().logout()
                .permitAll()
                .logoutSuccessHandler(customizeLogoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .and().formLogin()
                .permitAll()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
//                异常处理(权限拒绝、登录失败等)
                .and().exceptionHandling()
//                匿名用户访问无权限资源时的异常处理
                .authenticationEntryPoint(authenticationEntryPoint)
                .and().sessionManagement()
                .maximumSessions(1)
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
//                .expiredUrl("/user/login")
                .maxSessionsPreventsLogin(true)

        ;
        ;


    }


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("password").roles("USER");
//    }


}