package com.softcell.gonogo.uaaserver.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class AuthUserResource {


    /**
     * Return the principal identifying the logged in user
     * @param user
     * @return
     */
    @RequestMapping("/me")
    public Principal getCurrentLoggedInUser(Principal user) {
        return user;
    }
}
