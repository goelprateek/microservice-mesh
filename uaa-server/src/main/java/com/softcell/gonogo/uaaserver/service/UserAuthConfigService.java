package com.softcell.gonogo.uaaserver.service;

import com.softcell.gonogo.uaaserver.model.Authority;
import com.softcell.gonogo.uaaserver.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class UserAuthConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthConfigService.class);

    @Autowired
    private  UserService userService;

    public User getUser(String email) {
        LOGGER.debug(" hitting UserAuthConfigService ");
        return userService.findByEmail(email).get();
    }

    public List<GrantedAuthority> getRights(User user) {
        List<GrantedAuthority> grantedAuthority = new LinkedList<>();
        Set<Authority> right = user.getRights();
        if (null != right && !right.isEmpty()) {
            right.stream().forEach(r -> grantedAuthority.add(new SimpleGrantedAuthority(r.getName())));
        }
        return grantedAuthority;
    }

}
