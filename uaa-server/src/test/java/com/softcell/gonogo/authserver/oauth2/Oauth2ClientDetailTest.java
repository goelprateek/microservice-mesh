package com.softcell.gonogo.authserver.oauth2;

import com.softcell.gonogo.uaaserver.model.ClientDetail;
import com.softcell.gonogo.uaaserver.service.ClientDetailService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

/**
 * Created by prateek on 24/5/17.
 */
@RunWith(SpringRunner.class)
public class Oauth2ClientDetailTest {


    @MockBean
    private ShaPasswordEncoder shaPasswordEncoder;

    private ClientDetail authClient;


    @Before
    public void setUp(){

        authClient = new ClientDetail();
        authClient.setId("9V2t04MJSe"); //RandomStringUtils.randomAlphanumeric(10)
        //authClient.setClientId("uiapplication");
        //authClient.setResourceIds(new HashSet<>(Arrays.asList("rest_api")));
        //authClient.setClientSecret(shaPasswordEncoder.encodePassword("secret",""));
        //authClient.setRefreshTokenValiditySeconds(4500);
        //authClient.setAccessTokenValiditySeconds(4500);
        //authClient.setAuthorities(new HashSet<>(Arrays.asList("trust", "read", "write")));
        //authClient.setAuthorizedGrantTypes(new HashSet<>(Arrays.asList("client_credentials", "authorization_code", "implicit", "password", "refresh_token")));
        //authClient.setScope(new HashSet<>(Arrays.asList("trust", "read", "write")));
        //authClient.setSecretRequired(true);
        authClient.setRegisteredRedirectUri(new HashSet<String>(){{add("http://localhost:8080/gateway");}});
        //clientDetailService.save(authClient);

    }

    @Test
    public void checkingAWESOME(){
        ClientDetailService clientDetailService = new ClientDetailService();
        ClientDetail expectedAuthClient = clientDetailService.save(authClient);

        Assert.assertEquals(expectedAuthClient.getId() , authClient.getId());


    }


}
