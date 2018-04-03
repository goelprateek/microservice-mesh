package com.softcell.gonogo.uaaserver.repository;

import com.softcell.gonogo.uaaserver.model.ClientDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by prateek on 24/5/17.
 */
@Repository
public interface ClientDetailRepository extends MongoRepository<ClientDetail,Serializable>{


    ClientDetail findByClientId(String clientId);

}
