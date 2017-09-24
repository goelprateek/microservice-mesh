package com.softcell.gonogo.resourceserver.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/")
public class ProtectedResource {


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String helloWorld(Principal principal) {
        return principal == null ? "Hello anonymous" : "Hello " + principal.getName();
    }

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "secret", method = RequestMethod.GET)
    @ResponseBody
    public String helloSecret(Principal principal) {
        return principal == null ? "Hello anonymous" : "S3CR3T  - Hello " + principal.getName();
    }


}
