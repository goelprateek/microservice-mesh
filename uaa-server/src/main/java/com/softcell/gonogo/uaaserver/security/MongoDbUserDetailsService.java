package com.softcell.gonogo.uaaserver.security;

import com.softcell.gonogo.uaaserver.exception.UserNotActivatedException;
import com.softcell.gonogo.uaaserver.model.User;
import com.softcell.gonogo.uaaserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MongoDbUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" Loading userDetails from HybridUserDetailsService based on username {} ", username);
        }

        if (null == username && username.isEmpty()) {
            throw new UsernameNotFoundException(" username is empty ");
        }

        String lowerCaseLogin = username.toLowerCase(Locale.ENGLISH);

        Optional<User> userFromDatabase = userService.findByEmail(username);

        return userFromDatabase.map(user -> {
            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + lowerCaseLogin + " was not activated");
            }


            List<SimpleGrantedAuthority> grantedAuthorities = user.getRights().stream().map(right -> new SimpleGrantedAuthority(right.getName())).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowerCaseLogin,
                    user.getPassword(),
                    grantedAuthorities);

        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowerCaseLogin + " was not found in the " +
                "database"));


    }
}
