package com.softcell.gonogo.uaaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UaaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UaaServerApplication.class, args);
	}
}
