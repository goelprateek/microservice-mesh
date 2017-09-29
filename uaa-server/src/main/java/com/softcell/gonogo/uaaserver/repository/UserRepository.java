package com.softcell.gonogo.uaaserver.repository;

import com.softcell.gonogo.uaaserver.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,Serializable> {

    Optional<User> findByEmail(String email);

    User findById(String id);

    @Override
    void delete(User entity);
}
