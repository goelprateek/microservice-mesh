package com.softcell.gonogo.uaaserver.repository;


import com.softcell.gonogo.uaaserver.model.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the Authority entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {
}
