package com.softcell.gonogo.authserver.service;

import com.softcell.gonogo.uaaserver.model.User;
import com.softcell.gonogo.uaaserver.repository.UserRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {

    @Autowired private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private User user;

    @Before
    public void setup(){

        passwordEncoder = new BCryptPasswordEncoder();
        user = user.builder().email("prateekg@softcell.com")
                .id("1")
                .password(passwordEncoder.encode("secret"))
                .rights(Arrays.asList("trust", "read", "write"))
                .dateCreated(new Date())
                .build();
    }


    @Test
    public void b_getUserById() throws Exception {

        User byId = userRepository.findById("1");

        assertEquals(user.getId(),byId.getId());

    }

    @Test
    public void a_save() throws Exception {

        User savedUser = userRepository.save(user);

        assertEquals(savedUser.getId(),user.getId());

    }

    @Test
    public void c_findByEmail() throws Exception {

        User userById = userRepository.findByEmail(user.getEmail());

        assertEquals(userById.getId(),user.getId());


    }

    @Test
    @Ignore
    public void delete() throws Exception {

        userRepository.delete(user);

    }

}