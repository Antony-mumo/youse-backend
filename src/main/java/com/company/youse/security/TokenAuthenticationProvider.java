package com.company.youse.security;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.company.youse.services.SessionService;
import com.company.youse.models.User;


/**
 * This class implements Spring's AuthenticationProvider interface.
 * In Spring's Security flow, the AuthenticationProvider receives an Authentication object
 * and checks whether the user should be authenticated or not
 *
 * For our purpose, since we are using JWT tokens, this provider checks:
 * 1. Whether the token provided in the Authentication object exists in the Sessions DAO
 * 2. Retrieves the User object that belongs to this token
 * 3. Checks if the User's email matches the one in the DAO
 * 4. If it does, it returns a valid Authentication object with auth.isAuthenticated = true;
 * 5. If not, it throws a BadCredentialsException
 */
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
    private SessionService sessionService;
    private JwtProvider tokenProvider;

    /**
     * Constructor
     * @param sessionService
     */
    public TokenAuthenticationProvider(SessionService sessionService) {
        this.sessionService = sessionService;
        tokenProvider = new JwtProvider();
    }

    /**
     * Main function that we need to override.
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String jwt  = authentication.getPrincipal().toString();


        if(jwt==null){
            throw new BadCredentialsException("Invalid token or token expired");
        }

        if (!sessionService.isTokenExist(jwt)) {
            throw new BadCredentialsException("Token not present in DAO");
        }

        String username = tokenProvider.getUserNameFromJwtToken(jwt);
        User user = sessionService.getUserFromToken(username);


        // Validate the User's credentials and return a valid Authentication object if all checks pass
        return  new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword(), getGrantedAuthorities(user));
    }

    /**
     * Get the User's granted Authorities (roles)
     * @param user
     * @return
     */
    private List<GrantedAuthority> getGrantedAuthorities(User user){
        List<GrantedAuthority> authorities = new ArrayList<>();

        logger.info("==> (tokenauth) UserProfile : {}", user.getRole());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));

        logger.info("==> (tokenauth) authorities : {}", authorities);
        return authorities;
    }

    /**
     * This method informs Spring what kind of Authentication object this provider supports
     * In our case, we are authenticating with a token so we are using Spring's PreAuthenticatedAuthenticationToken
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }
}