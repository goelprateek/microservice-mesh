package com.softcell.gonogo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClientHttpRequestFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.sleuth.Span;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * API Gateway
 */
@SpringBootApplication
@EnableZuulProxy
@EnableDiscoveryClient
public class GatewayApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}


	@Bean
	UserInfoRestTemplateCustomizer userInfoRestTemplateCustomizer(SpringClientFactory springClientFactory) {
		return (OAuth2RestTemplate template) -> {
			AccessTokenProviderChain accessTokenProviderChain = Stream
					.of(
							new AuthorizationCodeAccessTokenProvider(),
							new ImplicitAccessTokenProvider(),
							new ResourceOwnerPasswordAccessTokenProvider(),
							new ClientCredentialsAccessTokenProvider())
					.peek(tp -> tp.setRequestFactory(new RibbonClientHttpRequestFactory(springClientFactory)))
					.collect(Collectors.collectingAndThen(Collectors.toList(), AccessTokenProviderChain::new));
			template.setAccessTokenProvider(accessTokenProviderChain);
		};
	}
}
