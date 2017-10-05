package com.softcell.gonogo.uaaserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Authentication user from data store
 */
@Service("hybridUserDetailsService")
public class HybridUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HybridUserDetailsService.class);


    private Collection<UserDetailsService> userDetailsServices = new LinkedList<>();

    public void addService(UserDetailsService userDetailsService) {
        this.userDetailsServices.add(userDetailsService);
    }

    public void addService(Collection<UserDetailsService> userDetailsService) {
        this.userDetailsServices.addAll(userDetailsService);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" Loading userDetails from HybridUserDetailsService based on username {} ", username);
        }

        if (null == username && username.isEmpty()) {
            throw new UsernameNotFoundException(" username is empty ");
        }

        String lowerCaseLogin = username.toLowerCase(Locale.ENGLISH);

        if (!CollectionUtils.isEmpty(userDetailsServices)) {

            for (UserDetailsService sv : userDetailsServices) {
                try {

                    final UserDetails userDetails = sv.loadUserByUsername(lowerCaseLogin);

                    if (null != userDetails) {
                        return userDetails;
                    }

                } catch (UsernameNotFoundException ex) {
                    LOGGER.error(" username {} not found in data store ", lowerCaseLogin);
                } catch (Exception ex) {
                    LOGGER.error(" unexpected exception caught while searching user in hybridUserDetailsService  ");
                    ex.printStackTrace();
                    throw ex;
                }
            }
        }

        throw new UsernameNotFoundException("Unknown user ");
    }

}
