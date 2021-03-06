package com.softcell.gonogo.resourceserver.rest;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/")
@RefreshScope
public class ProtectedResource {


    public Collection<String> fallback() {
        return new ArrayList<>();
    }

    @Value("${test.key:keeys}")
    private String test;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @HystrixCommand(fallbackMethod = "fallback")
    public String helloWorld(OAuth2Authentication principal) {
        return principal == null ? "Hello anonymous " : "Hello " + test + principal.getName();
    }

    @PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "secret", method = RequestMethod.GET)
    @ResponseBody
    public String helloSecret(Principal principal) {
        return principal == null ? "Hello anonymous" : "S3CR3T  - Hello " + principal.getName();
    }


}
