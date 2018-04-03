package com.softcell.gonogo.authserver.service;

import com.softcell.gonogo.uaaserver.UaaServerApplication;
import com.softcell.gonogo.uaaserver.model.ClientDetail;
import com.softcell.gonogo.uaaserver.repository.ClientDetailRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UaaServerApplication.class)
@DataMongoTest
public class ClientDetailServiceTest {

    @Autowired
    private ClientDetailRepository clientDetailsRepository;

    private ClientDetail authClient;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setup(){

        authClient = new ClientDetail();
        authClient.setId(RandomStringUtils.randomAlphanumeric(10));
        authClient.setClientId("sso-auth-client");
        authClient.setResourceIds(new HashSet<>(Arrays.asList("rest_api")));
        authClient.setClientSecret("mySecret");
        authClient.setRefreshTokenValiditySeconds(4500);
        authClient.setAccessTokenValiditySeconds(4500);
        authClient.setAuthorities(new HashSet<>(Arrays.asList("trust", "read", "write")));
        authClient.setAuthorizedGrantTypes(new HashSet<>(Arrays.asList("client_credentials", "authorization_code", "implicit", "password", "refresh_token")));
        authClient.setScope(new HashSet<>(Arrays.asList("trust", "read", "write")));
        authClient.setSecretRequired(true);

    }


    @Test
    public void save() throws Exception {

        ClientDetail save = clientDetailsRepository.save(authClient);

        assertEquals(save.getClientId(), authClient.getClientId());

    }
}