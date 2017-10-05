package com.softcell.gonogo.uaaserver.repository;

import com.softcell.gonogo.uaaserver.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for the User entity.
 */
@Repository
public interface UserRepository extends MongoRepository<User,Serializable> {

    Optional<User> findByEmail(String email);

    User findById(String id);

    @Override
    void delete(User entity);

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLogin(String login);

    Page<User> findAllByLoginNot(Pageable pageable, String login);

}
