package com.softcell.gonogo.socialclient.config;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ServletCustomizer {

    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer(){
        return container -> {
            container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED,"/unauthenticated"));
        };

    }
}
