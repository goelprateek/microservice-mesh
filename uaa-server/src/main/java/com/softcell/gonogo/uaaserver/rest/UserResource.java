package com.softcell.gonogo.uaaserver.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.security.Principal;

@SessionAttributes("authorizationRequest")
@RestController
public class UserResource {

    @GetMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
