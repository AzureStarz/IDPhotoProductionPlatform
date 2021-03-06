package com.example.springbootexample.config;

import com.example.springbootexample.service.impl.UserManagementServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter {
    final
    UserManagementServiceImpl userManagementService;

    public MyWebSecurityConfig(UserManagementServiceImpl userManagementService) {
        this.userManagementService = userManagementService;
    }

    //qxk 强散列哈希加密实现
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }

    //qxk 过滤器
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/index.html","/img/**","/fonts/**","/favicon.ico");
    }

    //qxk 身份认证接口
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userManagementService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // qxk 鉴权认证
        http.authorizeRequests().antMatchers("/api/fillin/**","/fillin/**","/register","/api/register", "/api/login").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("http://localhost:3000/login").loginProcessingUrl("/api/login").successHandler(
                new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        Object principal = authentication.getPrincipal();
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        //  loginService.login(asdasd //

                        httpServletResponse.setStatus(200);
                        out.write("success");
                        out.flush();
                        out.close();
                    }
                }
        ).failureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                httpServletResponse.setContentType("application/json;charset=utf-8");
                PrintWriter out = httpServletResponse.getWriter();
                httpServletResponse.setStatus(200);
                out.write("wrong");
                out.flush();
                out.close();
            }
        }).permitAll()
                .and().logout().logoutUrl("/api/logout").clearAuthentication(true).invalidateHttpSession(true).addLogoutHandler(new LogoutHandler() {
            @Override
            public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
            }
        }).logoutSuccessHandler(new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
            }
        })
                .and().csrf().disable();
    }
}
