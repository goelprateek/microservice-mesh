package com.softcell.gonogo.uaaserver.service;

import com.softcell.gonogo.uaaserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserAuthProviderService implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthProviderService.class);


    @Autowired
    private UserAuthConfigService authConfigService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    private Authentication signInUser(User user, List<GrantedAuthority> roles) {
        UserDetails springSecurityUser = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getId(), roles);
        Authentication authentication = new UsernamePasswordAuthenticationToken(springSecurityUser, user.getId(), roles);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {

        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("Authenticating using UserAuthProviderService: ");
        }

        String email = a.getName();
        String password = a.getCredentials().toString();
        User user = authConfigService.getUser(email);
        if (null != user) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                List<GrantedAuthority> roleAuthority = authConfigService.getRights(user);
                return signInUser(user, roleAuthority);
            }
            throw new AuthenticationException("Password for '" + email + "' not correct.") {
            };
        }

        throw new AuthenticationException("Could not find user with name '" + email + "'") {
        };
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
}
