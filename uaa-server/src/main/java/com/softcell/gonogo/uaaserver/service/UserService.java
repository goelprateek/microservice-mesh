package com.softcell.gonogo.uaaserver.service;

import com.softcell.gonogo.uaaserver.model.User;
import com.softcell.gonogo.uaaserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(String id) {
        return userRepository.findOne(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

}
