package com.company.youse.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class handles all authorization errors and displays a custom response
 */
@Component
public class AuthEntryPoint extends BasicAuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
            throws IOException /*, ServletException*/{
        logger.error("Unauthorized error. Message - {}", authEx.getMessage());
        response.addHeader("WWW-Authenticate", "Basic realm=" +getRealmName());
        response.setStatus(401);
        PrintWriter writer = response.getWriter();
        writer.println("HTTP Status 401 - " + authEx.getMessage());
    }

    @Override
    public void afterPropertiesSet() /*throws Exception*/ {
        setRealmName("Youse");
        super.afterPropertiesSet();
    }
}