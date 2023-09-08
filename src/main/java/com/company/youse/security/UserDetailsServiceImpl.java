package com.company.youse.security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.youse.models.User;
import com.company.youse.services.UserService;


/**
 * This class implements Spring's UserDetailsService interface
 * Whenever an Authentication Provider is prompted to check if a user is valid,
 * through this interface we are retrieving the User object from the DAO, and if the User
 * exists, a UserDetails object is created and passed to Spring Security for further checks.
 */
@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private UserService userService;
    private Converter<User, UserDetails> userUserDetailsConverter;

    @Autowired
    public void setUserService(UserService userService) {
        logger.info(" ==> Setting user service ...");
        this.userService = userService;
    }

    @Autowired
    @Qualifier(value = "userToUserDetails")
    public void setUserUserDetailsConverter(Converter<User, UserDetails> userUserDetailsConverter) {
        this.userUserDetailsConverter = userUserDetailsConverter;
    }

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Check whether a User exists in the Dao with the provided email
        User user = userService.findByEmail(email);
        logger.info("==> User : {}", user);
        if(user==null){
            logger.info("==> User not found");
            throw new UsernameNotFoundException("Username not found");
        }

        // If the user exists, convert to UserDetails and return to Spring
        return userUserDetailsConverter.convert(user);
    }
}
