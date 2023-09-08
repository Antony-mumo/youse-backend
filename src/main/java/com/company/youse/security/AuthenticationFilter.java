package com.company.youse.security;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;


/**
 * This filter extends Spring's Generic filter and implements our custom authentication functionality
 * We add this filter at the beggining of Spring's filter chain so every request passes through this one first
 *
 * The AuthenticationFilter:
 * 1. Receives an http request and retrieves the headers
 *
 * 2. If we have a login request, Basic Auth's Authorization header should be present (with "Basic " prefix)
 *      2.1 Decode the Authorization header provided to retrieve the user's credentials
 *      2.2 Create an Authentication object with the credentials
 *      2.3 Pass the Authentication object to the DaoAuthenticationProvider and retrieve a valid/invalid authentication
 *      2.4 If the Authentication passes, update Spring's SecurityContext so that the request can reach the secured endpoint
 *      2.5 If not, generate the appropriate error and pass the request to the rest of the filter chain to handle it
 *
 * 3. If we have a request to any other endpoint, the Authorization header should be present with a JWT token ("Bearer " prefix)
 *      3.1 Create an Authentication object with the JWT token
 *      3.2 Pass the Authentication object to the TokenAuthenticationProvider and retrieve a valid/invalid authentication
 *      3.3 If the Authentication passes, update Spring's SecurityContext so that the request can reach the secured endpoint
 *      3.4 If not, generate the appropriate error and pass the request to the rest of the filter chain to handle it
 *
 * 4. If no Basic Auth header, or JWT is present, clear the SecurityContect and throw a 401 Unauthorized error back
 */
public class AuthenticationFilter extends OncePerRequestFilter {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String TOKEN_SESSION_KEY = "token";
    private static final String USER_SESSION_KEY = "user";
    private AuthenticationManager authenticationManager;
    private String credentialsCharset = "UTF-8";

    private JwtProvider tokenProvider;

    /**
     * Constructor
     * @param authenticationManager
     */
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }


//    /**
//     * Main function that we have to override. doFilter implements our filter's custom logic
//     * @param request
//     * @param response
//     * @param chain
//     * @throws IOException
//     * @throws ServletException
//     */
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//
//    }



    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }


    private Authentication processUsernamePasswordAuthentication(HttpServletResponse httpResponse, String username, String password) throws IOException {
        logger.info("processUsernamePasswordAuth");
        // Create an Authentication object with the users credentials and try to authenticate
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        httpResponse.addHeader("Content-Type", "application/json");
        if(resultOfAuthentication.isAuthenticated()){
            logger.info("processUsernamePasswordAuth : isAuthenticated = true");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
        }
        else{
            logger.info("processUsernamePasswordAuth : isAuthenticated = false");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // Set the Security context's authentication to be the one we received from the auth provider.
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        return resultOfAuthentication;
    }


    private Authentication tryToAuthenticateWithUsernameAndPassword(String username, String password) {
        logger.info("tryToAuthenticateWithUsernameAndPassword");
        // Pass the Authentication object to the DaoAuthenticationProvider and receive a valid/invalid response
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }


    private Authentication processJwtTokenAuthentication(String jwt,HttpServletRequest request) {
        logger.info("processJwtTokenAuthentication");
        // Create an Authentication object from the JWT and try to authenticate
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameFromJWT(jwt,request);
        // Set the Security context's authentication to be the one we received from the auth provider.
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        return resultOfAuthentication;
    }

    private Authentication tryToAuthenticateWithUsernameFromJWT(String jwt,HttpServletRequest request) {
        logger.info("tryToAuthenticateWithUsernameFromJWT");
        // Pass the Authentication object to the TokenAuthenticationProvider and receive a valid/invalid response
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(jwt, null);
        return tryToAuthenticate(requestAuthentication);
    }


    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        logger.info("tryToAuthenticate");
        // Pass the Authentication object to the Authentication Manager.
        // If the Authentication object is of type UsernamePasswordAuthenticationToken, the Manager will delegate to DaoAuthenticationProvider
        // If the Authentication object is of type PreAuthenticatedAuthenticationToken, the Manager will delegate to TokenAuthenticationProvider
        Authentication responseAuthentication = this.authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            logger.info("Unable to authenticate Domain User for provided credentials");
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        logger.info("User successfully authenticated");
        return responseAuthentication;
    }

    /**
     * Extract username and password from Basic Auth header
     * @param header
     * @return
     * @throws IOException
     */
    private String[] extractAndDecodeHeader(String header)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decodeBase64(base64Token);
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, getCredentialsCharset());

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] { token.substring(0, delim), token.substring(delim + 1) };
    }

    /**
     * Get the JWT token by removing the Bearer prefix
     * @param request
     * @return
     */
    private String getJwt(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ","");
        }

        return null;
    }

    private String getCredentialsCharset() {
        return this.credentialsCharset;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        // Get the Authorization header, if it exists
        Optional<String> authHeader = Optional.ofNullable(httpRequest.getHeader("Authorization"));

        // Get the JWT, if it exists
        String jwt = getJwt(httpRequest);
        // Initialize our custom Jwt Utility class
        tokenProvider = new JwtProvider();

        try {
            // If we are trying to login, the Authorization header should have a Basic token
            if (authHeader.isPresent() && authHeader.get().startsWith("Basic ")) {
                logger.info("Trying to authenticate user by Basic Auth method. Token: {}", authHeader.get());
                // Decode the Basic Auth header and retrieve the user's credentials
                String[] userCredentials = extractAndDecodeHeader(authHeader.get());
                logger.info("Decoded-Username : {}", userCredentials[0]);
                logger.info("Decoded-Password : {}", userCredentials[1]);
                processUsernamePasswordAuthentication(httpResponse, userCredentials[0], userCredentials[1]);
            }
            else {
                // If we are trying to access any other resource, the Authorization header should have a Bearer token
                if (jwt!=null && tokenProvider.validateJwtToken(jwt)) {
                    logger.info("Trying to authenticate user by JWT Token method. Token: {}", jwt);
                    processJwtTokenAuthentication(jwt,httpRequest);
                }
                // If no Authorization header is present, clear Security context so that 401 error will be thrown later
                else{
                    SecurityContextHolder.clearContext();
                    logger.error("No token or authorization header was found");
                }
            }

            // When all checks are done from our custom filter, pass the request to the rest of Spring's filter chain
            logger.info("AuthenticationFilter is passing request down the filter chain");
            filterChain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            logger.error("Internal authentication service exception", internalAuthenticationServiceException);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            logger.error("Authentication exception : Bad credentials");
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Can NOT set user authentication", e);
        } finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }
}
