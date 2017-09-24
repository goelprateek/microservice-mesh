package com.softcell.gonogo.authserver;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prateek on 23/5/17.
 */
public class TokenTest {

    private String refreshToken;

    private String obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "mySecret").and().with().params(params).when().post("http://localhost:9091/uaa/oauth/token");
        refreshToken = response.jsonPath().getString("refresh_token");
        return response.jsonPath().getString("access_token");
    }

    private String obtainRefreshToken(String clientId) {
        final Map<String, String> params = new HashMap<>();
        params.put("grant_type", "refresh_token");
        params.put("client_id", clientId);
        params.put("refresh_token", refreshToken);
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:9091/uaa/oauth/token");
        return response.jsonPath().getString("access_token");
    }

    private void authorizeClient(String clientId) {
        final Map<String, String> params = new HashMap<>();
        params.put("response_type", "code");
        params.put("client_id", clientId);
        params.put("scope", "read,write");
        final Response response = RestAssured.given().auth().preemptive().basic(clientId, "secret").and().with().params(params).when().post("http://localhost:9091/auth-server/oauth/authorize");
    }

    @Test
    public void givenDBUser_whenRevokeToken_thenAuthorized() {
        final String accessToken = obtainAccessToken("sso-auth-client", "prateekg@softcell.com", "secret");
        Assert.assertNotNull(accessToken);
    }

}
