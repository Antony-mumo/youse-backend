package com.company.youse.security;

import com.company.youse.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableAutoConfiguration
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationEntryPoint authEntryPoint;

    //todo check if this breaks
    @Qualifier("UserDetailsServiceImpl")
    private final UserDetailsService userDetailsService;

    private final SessionService sessionService;

    /**
     * This method tells Spring security which Authentication Providers to use.
     * In our case we have 2 custom, DaoAuthenticationProvider and TokenAuthenticationProvider
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider())
                .authenticationProvider(tokenAuthenticationProvider())
        ;
    }

    private static final String[] AUTH_LIST_SWAGGER = {
            // -- swagger ui
            "**/swagger-resources/**", "/swagger-ui.html/**", "/v2/api-docs", "/webjars/**"
    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_LIST_SWAGGER);
        web.ignoring().mvcMatchers(AUTH_LIST_SWAGGER);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //todo change the lines below if you want to secure more endpoints
        //* Authorization and authentication config.
        // The lines below tell Spring which endpoints to secure (and what roles one needs to access them)
        http.authorizeRequests()
//                .antMatchers("/deleteUser").access("hasRole('ADMIN')")
                .antMatchers(HttpMethod.OPTIONS, "**").permitAll()
                .antMatchers("/api/signup",
                        "/api/customer/signup",
                        "/api/service-provider/signup",
                        "/api/forgotPassword",
                        "/api/signin",
                        "/api/oauth2/token",
                        "/api/{accountType}/oauth2/token",
                        "/api/user/check-email/*",
                        "/api/avatar/*",
                        "/api/test",
                        "/api/user/{account}/public/profile")
                .permitAll()
                .antMatchers("/actuator/**", "/v2/api-docs").permitAll()
                .anyRequest().authenticated();

        // HTTP Session Policy (stateless if no sessions are used)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //disabled, included a simpleCORSfilter file, that best performs this purpose
        // Add CORS headers for 2xx, 4xx and 5xx responses
//        http.cors();

        http.csrf().disable()
                //commented out, no longer supported in springboot 2.x.x
//                .authorizeRequests().anyRequest().authenticated().and()
                .httpBasic();

        // Auth entry point
        http.exceptionHandling().authenticationEntryPoint(authEntryPoint);

        // Add our custom AuthenticationFilter to the beggining of Spring's filter chain
        http.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(getPasswordEncoder()); // No encoding
        authenticationProvider.setPasswordEncoder(passwordEncoder());  // BCrypt encoding
        return authenticationProvider;
    }

    @Bean
    public AuthEntryPoint getAuthEntryPoint() {
        return new AuthEntryPoint();
    }

    //This is the very first method called while project build. In our project it is not playing any role. If we comment this then also everything will work perfectly because
    //Spring use it by default, this method is used if we want to change something in this method. Learn more about AuthenticationTrustResolver when required.
    @Bean
    public AuthenticationTrustResolver getAuthenticationTrustResolver() {
        return new AuthenticationTrustResolverImpl();
    }

    @Bean
    public AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(sessionService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Default password encoder, no encoding
     * @return
     */
//    private PasswordEncoder getPasswordEncoder() {
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence charSequence) {
//                return charSequence.toString();
//            }
//
//            @Override
//            public boolean matches(CharSequence charSequence, String s) {
//                return true;
//            }
//        };
//    }
}
