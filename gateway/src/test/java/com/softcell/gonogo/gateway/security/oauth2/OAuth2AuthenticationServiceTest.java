package com.softcell.gonogo.gateway.security.oauth2;

import com.softcell.gonogo.gateway.config.GoNoGoProperties;
import com.softcell.gonogo.gateway.config.oauth2.OAuth2Properties;
import com.softcell.gonogo.gateway.web.filter.RefreshTokenFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.*;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * Test password and refresh token grants.
 *
 * @see OAuth2AuthenticationService
 */
@RunWith(MockitoJUnitRunner.class)
public class OAuth2AuthenticationServiceTest {

    public static final String CLIENT_AUTHORIZATION = "Basic c3NvLWF1dGgtY2xpZW50Om15U2VjcmV0";
    public static final String ACCESS_TOKEN_VALUE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidWFhLXNlcnZlciIsImF1dGgtc2VydmVyIiwicmVzdF9hcGkiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInRydXN0IiwicmVhZCIsIndyaXRlIl0sImV4cCI6MTUwNzA1MjgzNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI4ZTJjYmMzOC1iMGY4LTRlMmEtYThhYi0yYzhiMGZmMGU3ZTYiLCJjbGllbnRfaWQiOiJzc28tYXV0aC1jbGllbnQifQ.Rm85uohTEj9s1idYStPCbTGE9tusXt3T_GZIfNwllfdzOoMp1y_bcrud01hTSCe1kLqE_4HxnyhSbTTLauDbJNM22tEShLR2G4xX8luZOR4OKtWyhqIz8QDkjjlb4Motzura_qfcKNZxjaaWNRfFf5_kKOHqZO2UIkMP4wEsr2OtzRcKJ57NEi7NU9QI-5ZpNpMVRqQpqnLuzsxUgsoFvaqzdu0eSsq3eaOZF-GgZw5_agQ_O0KJWwIWJP4B10nGQqwVa2n-8117b1jLaZyCaL6oLYyfqZeUn1Y1GKoIjQ_iYaRb6pECmYj901umgxnXi0XKGfr_40Z9WhTlZF4RwQ";
    public static final String REFRESH_TOKEN_VALUE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidWFhLXNlcnZlciIsImF1dGgtc2VydmVyIiwicmVzdF9hcGkiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInRydXN0IiwicmVhZCIsIndyaXRlIl0sImF0aSI6IjhlMmNiYzM4LWIwZjgtNGUyYS1hOGFiLTJjOGIwZmYwZTdlNiIsImV4cCI6MTUwNzA1NjMzNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZWE0NmUyMi1hMTBmLTRmODEtYWEzNS04MzE1NDcxZDJjODgiLCJjbGllbnRfaWQiOiJzc28tYXV0aC1jbGllbnQifQ.U0uHFBEsIU5HtGGu5BAM9MtoqBCK6cozezhgJ4ML4-xX-j4CRsiCs9x6__lcylINJkU-h585U10Jtrjidt1hv46zKGsEY5UrFfuNzTdUAJuC9p06nXyDoOpOUhXODYWA-ZZo5zRHJ9_GfQhhj0kitomDYuZHt6N2f_N9g0TJ76ccaj22e5t1Vur_tK3y8lCY6ZxeiZ6VzCXw6q-Tf-V_T8_en1rUKzj94Hnbs2DFMt0M3355g9qRYBHZQRKRm3d1M6vU9UCZ2crzXEbfETQUKTmvYT304QFS-xTwKULwgSdmDxTuxWoDCpA5XeVUzpwESv0z5R3Vn7RkmAE5foEUGQ";
    public static final String NEW_ACCESS_TOKEN_VALUE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidWFhLXNlcnZlciIsImF1dGgtc2VydmVyIiwicmVzdF9hcGkiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInRydXN0IiwicmVhZCIsIndyaXRlIl0sImV4cCI6MTUwNzA1MjgzNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiI4ZTJjYmMzOC1iMGY4LTRlMmEtYThhYi0yYzhiMGZmMGU3ZTYiLCJjbGllbnRfaWQiOiJzc28tYXV0aC1jbGllbnQifQ.Rm85uohTEj9s1idYStPCbTGE9tusXt3T_GZIfNwllfdzOoMp1y_bcrud01hTSCe1kLqE_4HxnyhSbTTLauDbJNM22tEShLR2G4xX8luZOR4OKtWyhqIz8QDkjjlb4Motzura_qfcKNZxjaaWNRfFf5_kKOHqZO2UIkMP4wEsr2OtzRcKJ57NEi7NU9QI-5ZpNpMVRqQpqnLuzsxUgsoFvaqzdu0eSsq3eaOZF-GgZw5_agQ_O0KJWwIWJP4B10nGQqwVa2n-8117b1jLaZyCaL6oLYyfqZeUn1Y1GKoIjQ_iYaRb6pECmYj901umgxnXi0XKGfr_40Z9WhTlZF4RwQ";
    public static final String NEW_REFRESH_TOKEN_VALUE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidWFhLXNlcnZlciIsImF1dGgtc2VydmVyIiwicmVzdF9hcGkiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInRydXN0IiwicmVhZCIsIndyaXRlIl0sImF0aSI6IjhlMmNiYzM4LWIwZjgtNGUyYS1hOGFiLTJjOGIwZmYwZTdlNiIsImV4cCI6MTUwNzA1NjMzNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZWE0NmUyMi1hMTBmLTRmODEtYWEzNS04MzE1NDcxZDJjODgiLCJjbGllbnRfaWQiOiJzc28tYXV0aC1jbGllbnQifQ.U0uHFBEsIU5HtGGu5BAM9MtoqBCK6cozezhgJ4ML4-xX-j4CRsiCs9x6__lcylINJkU-h585U10Jtrjidt1hv46zKGsEY5UrFfuNzTdUAJuC9p06nXyDoOpOUhXODYWA-ZZo5zRHJ9_GfQhhj0kitomDYuZHt6N2f_N9g0TJ76ccaj22e5t1Vur_tK3y8lCY6ZxeiZ6VzCXw6q-Tf-V_T8_en1rUKzj94Hnbs2DFMt0M3355g9qRYBHZQRKRm3d1M6vU9UCZ2crzXEbfETQUKTmvYT304QFS-xTwKULwgSdmDxTuxWoDCpA5XeVUzpwESv0z5R3Vn7RkmAE5foEUGQ";
    public static final String EXPIRED_SESSION_TOKEN_VALUE = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidWFhLXNlcnZlciIsImF1dGgtc2VydmVyIiwicmVzdF9hcGkiXSwidXNlcl9uYW1lIjoidXNlciIsInNjb3BlIjpbInRydXN0IiwicmVhZCIsIndyaXRlIl0sImF0aSI6IjhlMmNiYzM4LWIwZjgtNGUyYS1hOGFiLTJjOGIwZmYwZTdlNiIsImV4cCI6MTUwNzA1NjMzNSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZWE0NmUyMi1hMTBmLTRmODEtYWEzNS04MzE1NDcxZDJjODgiLCJjbGllbnRfaWQiOiJzc28tYXV0aC1jbGllbnQifQ.U0uHFBEsIU5HtGGu5BAM9MtoqBCK6cozezhgJ4ML4-xX-j4CRsiCs9x6__lcylINJkU-h585U10Jtrjidt1hv46zKGsEY5UrFfuNzTdUAJuC9p06nXyDoOpOUhXODYWA-ZZo5zRHJ9_GfQhhj0kitomDYuZHt6N2f_N9g0TJ76ccaj22e5t1Vur_tK3y8lCY6ZxeiZ6VzCXw6q-Tf-V_T8_en1rUKzj94Hnbs2DFMt0M3355g9qRYBHZQRKRm3d1M6vU9UCZ2crzXEbfETQUKTmvYT304QFS-xTwKULwgSdmDxTuxWoDCpA5XeVUzpwESv0z5R3Vn7RkmAE5foEUGQ";
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    public GoNoGoProperties goNoGoProperties;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private TokenStore tokenStore;
    private OAuth2TokenEndpointClient authorizationClient;
    private OAuth2AuthenticationService authenticationService;
    private OAuth2Properties oAuth2Properties;
    private RefreshTokenFilter refreshTokenFilter;

    public static OAuth2AccessToken createAccessToken(String accessTokenValue, String refreshTokenValue) {

        DefaultOAuth2AccessToken accessToken = new DefaultOAuth2AccessToken(accessTokenValue);
        accessToken.setExpiration(new Date());
        DefaultOAuth2RefreshToken refreshToken = new DefaultOAuth2RefreshToken(refreshTokenValue);
        accessToken.setRefreshToken(refreshToken);
        return accessToken;

    }

    public static MockHttpServletRequest createMockHttpServletRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "http://www.gonogo.com");
        Cookie accessTokenCookie = new Cookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE, ACCESS_TOKEN_VALUE);
        Cookie refreshTokenCookie = new Cookie(OAuth2CookieHelper.REFRESH_TOKEN_COOKIE, REFRESH_TOKEN_VALUE);
        request.setCookies(accessTokenCookie, refreshTokenCookie);
        return request;
    }

    @Before
    public void init() {
        oAuth2Properties = new OAuth2Properties();
        goNoGoProperties = new GoNoGoProperties();
        goNoGoProperties.getSecurity().getClientAuthorization().setAccessTokenUri("http://auth-server/uaa/outh/token");
        OAuth2CookieHelper cookieHelper = new OAuth2CookieHelper(oAuth2Properties);
        OAuth2AccessToken accessToken = createAccessToken(ACCESS_TOKEN_VALUE, REFRESH_TOKEN_VALUE);

        mockPasswordGrant(accessToken);
        mockRefreshGrant();

        authorizationClient = new UaaTokenEndpointClient(restTemplate, goNoGoProperties, oAuth2Properties);

        authenticationService = new OAuth2AuthenticationService(authorizationClient, cookieHelper);

        when(tokenStore.readAccessToken(ACCESS_TOKEN_VALUE)).thenReturn(accessToken);

        refreshTokenFilter = new RefreshTokenFilter(authenticationService, tokenStore);

    }

    private void mockRefreshGrant() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", REFRESH_TOKEN_VALUE);
        //we must authenticate with the UAA server via HTTP basic authentication using the browser's client_id with no client secret
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", CLIENT_AUTHORIZATION);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        OAuth2AccessToken newAccessToken = createAccessToken(NEW_ACCESS_TOKEN_VALUE, NEW_REFRESH_TOKEN_VALUE);

        when(restTemplate.postForEntity("http://auth-server/uaa/oauth/token", entity, OAuth2AccessToken.class))
                .thenReturn(new ResponseEntity<>(newAccessToken, HttpStatus.OK));

        //headers missing -> unauthorized
        HttpEntity<MultiValueMap<String, String>> headerlessEntity = new HttpEntity<>(params, new HttpHeaders());

        when(restTemplate.postForEntity("http://auth-server/uaa/oauth/token", headerlessEntity, OAuth2AccessToken.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
    }

    private void mockPasswordGrant(OAuth2AccessToken accessToken) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", CLIENT_AUTHORIZATION);

        LinkedMultiValueMap<String, String> formParam = new LinkedMultiValueMap<>();
        formParam.set("username", "user");
        formParam.set("password", "padd");
        formParam.set("grant_type", "password");

        HttpEntity<MultivaluedMap<String, String>> entity = new HttpEntity<>(formParam);

        when(restTemplate.postForEntity("http://auth-server//uaa/oauth/token", entity, OAuth2AccessToken.class))
                .thenReturn(new ResponseEntity<>(accessToken, HttpStatus.OK));


    }

    @Test
    public void testAuthenticationCookies() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setServerName("www.gonogo.com");

        request.addHeader("Authorization", CLIENT_AUTHORIZATION);

        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "padd");
        params.put("rememberMe", "true");

        MockHttpServletResponse response = new MockHttpServletResponse();

        authenticationService.authenticate(request, response, params);

        // checking if cookies are set properly
        Cookie accessTokenCookie = response.getCookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE);

        Assert.assertEquals(ACCESS_TOKEN_VALUE, accessTokenCookie.getValue());

        Cookie refreshTokenCookie = response.getCookie(OAuth2CookieHelper.REFRESH_TOKEN_COOKIE);

        Assert.assertEquals(REFRESH_TOKEN_VALUE, refreshTokenCookie.getValue());

        Assert.assertTrue(OAuth2CookieHelper.isRememberMe(refreshTokenCookie));

    }

    @Test
    public void testAuthenticationNoRememberMe() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setServerName("www.google.com");

        Map<String, String> params = new HashMap<>();
        params.put("username", "user");
        params.put("password", "padd");
        params.put("rememberMe", "false");

        MockHttpServletResponse response = new MockHttpServletResponse();

        authenticationService.authenticate(request, response, params);

        //check that cookies are set correctly
        Cookie accessTokenCookie = response.getCookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE);

        Assert.assertEquals(ACCESS_TOKEN_VALUE, accessTokenCookie.getValue());

        Cookie refreshTokenCookie = response.getCookie(OAuth2CookieHelper.SESSION_TOKEN_COOKIE);

        Assert.assertEquals(REFRESH_TOKEN_VALUE, refreshTokenCookie);

        Assert.assertFalse(OAuth2CookieHelper.isRememberMe(refreshTokenCookie));

    }

    @Test
    public void testRefreshGrant() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        HttpServletRequest newRequest = refreshTokenFilter.refreshTokensIfExpiring(request, response);
        Cookie newAccessTokenCookie = response.getCookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE);
        Assert.assertEquals(NEW_ACCESS_TOKEN_VALUE, newAccessTokenCookie.getValue());
        Cookie newRefreshTokenCookie = response.getCookie(OAuth2CookieHelper.REFRESH_TOKEN_COOKIE);
        Assert.assertEquals(NEW_REFRESH_TOKEN_VALUE, newRefreshTokenCookie.getValue());
        Cookie requestAccessTokenCookie = OAuth2CookieHelper.getAccessTokenCookie(newRequest);
        Assert.assertEquals(NEW_ACCESS_TOKEN_VALUE, requestAccessTokenCookie.getValue());
    }

    @Test
    public void testSessionExpired() {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "http://www.test.com");
        Cookie accessTokenCookie = new Cookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE, ACCESS_TOKEN_VALUE);
        Cookie refreshTokenCookie = new Cookie(OAuth2CookieHelper.SESSION_TOKEN_COOKIE, EXPIRED_SESSION_TOKEN_VALUE);
        request.setCookies(accessTokenCookie, refreshTokenCookie);
        MockHttpServletResponse response = new MockHttpServletResponse();
        HttpServletRequest newRequest = refreshTokenFilter.refreshTokensIfExpiring(request, response);
        //cookies in response are deleted
        Cookie newAccessTokenCookie = response.getCookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE);
        Assert.assertEquals(0, newAccessTokenCookie.getMaxAge());
        Cookie newRefreshTokenCookie = response.getCookie(OAuth2CookieHelper.REFRESH_TOKEN_COOKIE);
        Assert.assertEquals(0, newRefreshTokenCookie.getMaxAge());
        //request no longer contains cookies
        Cookie requestAccessTokenCookie = OAuth2CookieHelper.getAccessTokenCookie(newRequest);
        Assert.assertNull(requestAccessTokenCookie);
        Cookie requestRefreshTokenCookie = OAuth2CookieHelper.getRefreshTokenCookie(newRequest);
        Assert.assertNull(requestRefreshTokenCookie);
    }

    /**
     * If no refresh token is found and the access token has expired, then expect an exception.
     */
    @Test
    public void testRefreshGrantNoRefreshToken() {
        MockHttpServletRequest request = new MockHttpServletRequest(HttpMethod.GET.name(), "http://www.test.com");
        Cookie accessTokenCookie = new Cookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE, ACCESS_TOKEN_VALUE);
        request.setCookies(accessTokenCookie);
        MockHttpServletResponse response = new MockHttpServletResponse();
        expectedException.expect(InvalidTokenException.class);
        refreshTokenFilter.refreshTokensIfExpiring(request, response);
    }

    @Test
    public void testLogout() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie accessTokenCookie = new Cookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE, ACCESS_TOKEN_VALUE);
        Cookie refreshTokenCookie = new Cookie(OAuth2CookieHelper.REFRESH_TOKEN_COOKIE, REFRESH_TOKEN_VALUE);
        request.setCookies(accessTokenCookie, refreshTokenCookie);
        MockHttpServletResponse response = new MockHttpServletResponse();
        authenticationService.logout(request, response);
        Cookie newAccessTokenCookie = response.getCookie(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE);
        Assert.assertEquals(0, newAccessTokenCookie.getMaxAge());
        Cookie newRefreshTokenCookie = response.getCookie(OAuth2CookieHelper.REFRESH_TOKEN_COOKIE);
        Assert.assertEquals(0, newRefreshTokenCookie.getMaxAge());
    }

    @Test
    public void testStripTokens() {
        MockHttpServletRequest request = createMockHttpServletRequest();
        HttpServletRequest newRequest = authenticationService.stripTokens(request);
        CookieCollection cookies = new CookieCollection(newRequest.getCookies());
        Assert.assertFalse(cookies.contains(OAuth2CookieHelper.ACCESS_TOKEN_COOKIE));
        Assert.assertFalse(cookies.contains(OAuth2CookieHelper.REFRESH_TOKEN_COOKIE));
    }

}