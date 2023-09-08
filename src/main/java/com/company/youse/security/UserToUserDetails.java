package com.company.youse.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.company.youse.models.User;


/**
 * This class implements Spring's Converter interface. It converts a User to UserDetails object
 * that is required by Spring Security
 */
@Component
public class UserToUserDetails implements Converter<User, UserDetails> {

    private static final Logger logger = LoggerFactory.getLogger(UserToUserDetails.class);

    @Override
    public UserDetails convert(User user) {
        UserDetailsImpl userDetails;

        if (user != null) {
            logger.info(" ==> converting User to UserDetails object ...");
            userDetails = new UserDetailsImpl(user);
        }
        else{
            logger.info(" ==> user object is null ...");
            throw new UsernameNotFoundException("User object is null");
        }

        return userDetails;
    }
}
