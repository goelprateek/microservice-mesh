package com.softcell.gonogo.authserver;

import com.softcell.gonogo.uaaserver.UaaServerApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = UaaServerApplication.class)
public class AuthServerIntegrationTests {

    @Autowired
    private WebApplicationContext wac;



    private MockMvc mockMvc;

    private static final String CLIENT_ID = "sso-auth-client";
    private static final String CLIENT_SECRET = "mySecret";

    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    private static final String EMAIL = "prateekg@softcell.com";
    private static final String NAME = "Jim";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .build();
    }

    @Test
    public void contextLoads() {
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);

        // @formatter:off

        ResultActions result = mockMvc.perform(post("http://localhost:9091/uaa/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));

        // @formatter:on

        String resultString = result.andReturn().getResponse().getContentAsString();
        System.out.println(resultString);
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();

    }

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {

        mockMvc.perform(get("http://localhost:8081/resource-server/secured"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenToken_whenPostGetSecureRequest_thenOk() throws Exception {

        final String accessToken = obtainAccessToken("prateekg@softcell.com", "secret");

        System.out.println("token:" + accessToken);

        mockMvc.perform(get("http://localhost:8081/resource-server/secured")
                .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk());

    }


}
