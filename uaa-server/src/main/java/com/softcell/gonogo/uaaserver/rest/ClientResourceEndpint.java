package com.softcell.gonogo.uaaserver.rest;


import com.softcell.gonogo.uaaserver.model.ClientDetail;
import com.softcell.gonogo.uaaserver.service.ClientDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

//@RestController("/api/clients")
public class ClientResourceEndpint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientResourceEndpint.class);


    /*@Autowired
    private ClientDetailService clientDetailService;


    @GetMapping
    public Collection<ClientDetail> getAllClient() {
        LOGGER.debug(" fetching all clients ");
        return clientDetailService.findAllClientDetails();
    }

    @PostMapping
    public ClientDetail updateClient(@Validated @RequestBody ClientDetail clientDetail) {
        LOGGER.debug(" updating client [{}] ", clientDetail);
        return clientDetailService.updateClientDetails(clientDetail);
    }

    @PutMapping
    public ClientDetail saveClient(@Valid @RequestBody ClientDetail clientDetail) {
        LOGGER.debug(" saving client {} ", clientDetail);
        return clientDetailService.save(clientDetail);
    }


    @PostMapping("/reset")
    public ClientDetail resetClientSecret(@Validated @RequestParam("clientId") String clientId,
                                          @Validated @RequestParam("secret") String secret) {
        return clientDetailService.resetClientSecret(clientId, secret);
    }

    @DeleteMapping(params = "clientId")
    public void deleteClientDetail(@NotNull @RequestParam("clientId") String clientId) {
        LOGGER.debug(" deleting clientId details for clientId '{}' ", clientId);
        clientDetailService.removeClientDetails(clientId);
    }*/


}
