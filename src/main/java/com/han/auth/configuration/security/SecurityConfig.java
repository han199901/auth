package com.han.auth.configuration.security;


import com.han.auth.configuration.property.CookieConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    public static class SecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {


        private RestAuthenticationFailureHandler restAuthenticationFailureHandler;
        private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
        private RestDetailsServiceImpl formDetailsService;
        private LoginAuthenticationEntryPoint loginAuthenticationEntryPoint;
        private  RestAuthenticationProvider restAuthenticationProvider;
        private RestAccessDeniedHandler restAccessDeniedHandler;
        private RestLogoutSuccessHandler restLogoutSuccessHandler;



        @Autowired
        public SecurityConfigurerAdapter(RestAuthenticationFailureHandler restAuthenticationFailureHandler, RestAuthenticationSuccessHandler restAuthenticationSuccessHandler, RestDetailsServiceImpl formDetailsService, LoginAuthenticationEntryPoint loginAuthenticationEntryPoint, RestAuthenticationProvider restAuthenticationProvider, RestAccessDeniedHandler restAccessDeniedHandler, RestLogoutSuccessHandler restLogoutSuccessHandler) {
            this.restAuthenticationFailureHandler = restAuthenticationFailureHandler;
            this.restAuthenticationSuccessHandler = restAuthenticationSuccessHandler;
            this.formDetailsService = formDetailsService;
            this.loginAuthenticationEntryPoint = loginAuthenticationEntryPoint;
            this.restAuthenticationProvider = restAuthenticationProvider;
            this.restAccessDeniedHandler = restAccessDeniedHandler;
            this.restLogoutSuccessHandler = restLogoutSuccessHandler;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //网页中允许使用 iframe 打开页面
            http.headers().frameOptions().disable();

//            List<String> securityIgnoreUrls = systemConfig.getSecurityIgnoreUrls();
//            String[] ignores = new String[securityIgnoreUrls.size()];

            http
                    .addFilterAt(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling().authenticationEntryPoint(loginAuthenticationEntryPoint)
                    .and().authenticationProvider(restAuthenticationProvider)
                    .authorizeRequests()


//                  我需要一个动态的角色权限

//                    .antMatchers(securityIgnoreUrls.toArray(ignores)).permitAll()
//                    .antMatchers("/api/admin/**").hasRole(RoleEnum.ADMIN.getName())
//                    .antMatchers("/api/student/**").hasRole(RoleEnum.STUDENT.getName())
//                    .antMatchers("/api/teacher/**").hasRole(RoleEnum.TEACHER.getName())
                    .anyRequest().permitAll()
                    .and().exceptionHandling().accessDeniedHandler(restAccessDeniedHandler)
                    .and().formLogin().successHandler(restAuthenticationSuccessHandler).failureHandler(restAuthenticationFailureHandler)
                    .and().logout().logoutUrl("/api/user/logout").logoutSuccessHandler(restLogoutSuccessHandler).invalidateHttpSession(true)
                    .and().rememberMe().key(CookieConfig.getName()).tokenValiditySeconds(CookieConfig.getInterval()).userDetailsService(formDetailsService)
                    .and().csrf().disable()
                    .cors();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            final CorsConfiguration configuration = new CorsConfiguration();
            configuration.setMaxAge(3600L);
            configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
            //configuration.setAllowedOrigins(Collections.singletonList("*"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.setAllowCredentials(true);
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/api/**", configuration);
            return source;
        }


        @Bean
        public RestLoginAuthenticationFilter authenticationFilter() throws Exception {
            RestLoginAuthenticationFilter authenticationFilter = new RestLoginAuthenticationFilter();
            authenticationFilter.setAuthenticationSuccessHandler(restAuthenticationSuccessHandler);
            authenticationFilter.setAuthenticationFailureHandler(restAuthenticationFailureHandler);
            authenticationFilter.setAuthenticationManager(authenticationManagerBean());
            authenticationFilter.setUserDetailsService(formDetailsService);
            return authenticationFilter;
        }
    }
}
