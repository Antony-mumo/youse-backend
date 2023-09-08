package com.company.youse.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.company.youse.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



/**
 * Implementaion of Spring's UserDetails interface. After Spring Security authenticates a User against the database
 * it returns a UserDetails object to handle the authorization
 */
public class UserDetailsImpl extends User implements UserDetails {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsImpl.class);
    private boolean enabled = true;
    private boolean accountExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
//    We are not using setAuthorities for now but will use when we have different roles in the project
//    private Collection<SimpleGrantedAuthority> authorities;


    public UserDetailsImpl() {}

    /**
     * Constructor from User object
     * @param user
     */
    public UserDetailsImpl(User user) {
        super(user);
    }

    /**
     * Get the Users granted Authorities (roles such as ROLE_USER, ROLE_ADMIN etc.)
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        // At this current version, every user is granted a SINGLE role.
        // If we want a user to have multiple roles, this section of code needs to change to
        // return a list of roles instead of a single one
        logger.info("==> User ROLE : {}", getRole());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+ getRole()));

        logger.info("==> Granted Authority : {}", authorities);
        return authorities;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

//    public void setAuthorities(Collection<SimpleGrantedAuthority> authorities) {
//        this.authorities = authorities;
//    }
}
