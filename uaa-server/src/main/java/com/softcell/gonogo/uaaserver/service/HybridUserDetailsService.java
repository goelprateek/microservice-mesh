package com.softcell.gonogo.uaaserver.service;

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
import java.util.stream.Collectors;

@Service
public class HybridUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HybridUserDetailsService.class);


    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Loading userDetails from HybridUserDetailsService based on username {} ",username);
        }

        if (null == username && username.isEmpty()) {
            throw new UsernameNotFoundException(" username is empty ");
        }

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        Object clientPrincipal = authentication.getPrincipal();

        if (clientPrincipal instanceof User) {
            return loadUser(username);
        }

        throw new UsernameNotFoundException(
                "Unauthorized client_id or username not found: " + username);
    }

    private UserDetails loadUser(String username) {

        com.softcell.gonogo.uaaserver.model.User userByEmail = userService.findByEmail(username);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(" Loaded user from  details: " + userByEmail);

        List<SimpleGrantedAuthority> authorities = userByEmail.getRights()
                .stream()
                .map(right -> new SimpleGrantedAuthority(right))
                .collect(Collectors.toList());

        if(null != userByEmail){
            return new User(userByEmail.getEmail(), "", authorities);
        }else{
            return  null;
        }
    }
}
