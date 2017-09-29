package com.softcell.gonogo.uaaserver.service;

import com.softcell.gonogo.uaaserver.exception.UserNotActivatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authentication user from data store
 */
@Service
public class HybridUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HybridUserDetailsService.class);


    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug(" Loading userDetails from HybridUserDetailsService based on username {} ",username);
        }

        String lowerCaseLogin = username.toLowerCase(Locale.ENGLISH);

        if (null == username && username.isEmpty()) {
            throw new UsernameNotFoundException(" username is empty ");
        }

        Optional<com.softcell.gonogo.uaaserver.model.User> userFromDatabase = userService.findByEmail(lowerCaseLogin);

        return userFromDatabase.map(user -> {
            if(!user.getIsActivated()){
                throw new UserNotActivatedException("User " + lowerCaseLogin + " was not activated");
            }

            List<SimpleGrantedAuthority> grantedAuthorities = user.getRights().stream().map(right -> new SimpleGrantedAuthority(right)).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowerCaseLogin,
                    user.getPassword(),
                    grantedAuthorities);

        }).orElseThrow( () -> new UsernameNotFoundException("User " + lowerCaseLogin + " was not found in the " +
                "database"));

    }

}
