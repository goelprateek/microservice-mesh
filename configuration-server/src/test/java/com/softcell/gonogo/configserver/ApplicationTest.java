package com.softcell.gonogo.configserver;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.config.server.test.ConfigServerTestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = ConfigurationServerApplication.class)
@AutoConfigureMockMvc
public class ApplicationTest {



    @Test
    public void configurationAvailable() {
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> entity = new TestRestTemplate().getForEntity(
                "http://localhost:9092"  + "/app/cloud", Map.class);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        System.out.println(entity.getBody());


    }


    @Test
    public void envPostAvailable() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        @SuppressWarnings("rawtypes")
        ResponseEntity<Map> entity = new TestRestTemplate().postForEntity(
                "http://localhost:9092" + "/admin/env", form, Map.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
    }

}
