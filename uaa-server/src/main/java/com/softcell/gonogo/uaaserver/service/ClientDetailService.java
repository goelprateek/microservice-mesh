package com.softcell.gonogo.uaaserver.service;

import com.softcell.gonogo.uaaserver.model.ClientDetail;
import com.softcell.gonogo.uaaserver.repository.ClientDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by prateek on 24/5/17.
 */
@Service("clientDetailService")
public class ClientDetailService implements ClientDetailsService, ClientRegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientDetailService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailRepository clientDetailsRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(" authenticating client_id {} ", clientId);
        }

        ClientDetail clientDetails = clientDetailsRepository.findByClientId(clientId);

        if (null == clientDetails) {
            throw new ClientRegistrationException("Client not found with id '" + clientId + "'");
        }

        return getClientDetailsFromClientDetail(clientDetails);

    }

    private ClientDetails getClientDetailsFromClientDetail(ClientDetail clientDetail) {

        BaseClientDetails bc = new BaseClientDetails();

        bc.setAccessTokenValiditySeconds(clientDetail.getAccessTokenValiditySeconds());

        bc.setAuthorizedGrantTypes(clientDetail.getAuthorizedGrantTypes());

        bc.setClientId(clientDetail.getClientId());

        bc.setClientSecret(clientDetail.getClientSecret());

        bc.setRefreshTokenValiditySeconds(clientDetail.getRefreshTokenValiditySeconds());

        bc.setRegisteredRedirectUri(clientDetail.getRegisteredRedirectUri());

        bc.setResourceIds(clientDetail.getResourceIds());

        bc.setScope(clientDetail.getScope());

        return bc;

    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {

        ClientDetail clientDetail = getClientDetailFromClientDetails(clientDetails);

        clientDetailsRepository.save(clientDetail);

    }


    private ClientDetail getClientDetailFromClientDetails(ClientDetails clientDetails) {

        return ClientDetail.builder().accessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds())
                .additionalInformation(clientDetails.getAdditionalInformation())
                .authorizedGrantTypes(clientDetails.getAuthorizedGrantTypes())
                .clientId(clientDetails.getClientId())
                .clientSecret(clientDetails.getClientSecret())
                .refreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds())
                .registeredRedirectUri(clientDetails.getRegisteredRedirectUri())
                .resourceIds(clientDetails.getResourceIds())
                .scope(clientDetails.getScope())
                .scoped(clientDetails.isScoped())
                .secretRequired(clientDetails.isSecretRequired())
                .id(clientDetails.getClientId()).build();

    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {

        ClientDetail clientDetail = clientDetailsRepository.findByClientId(clientDetails.getClientId());

        if (null == clientDetail) {

            throw new NoSuchClientException(" Client not found with ID '" + clientDetails.getClientId() + "'");
        }

        clientDetail = getClientDetailFromClientDetails(clientDetails);

        clientDetailsRepository.save(clientDetail);

    }

    public ClientDetail updateClientDetails(ClientDetail clientDetail) {

        ClientDetail byClientId = clientDetailsRepository.findByClientId(clientDetail.getClientId());

        if (null == clientDetail) {

            throw new NoSuchClientException(" Client not found with ID '" + clientDetail.getClientId() + "'");
        }

        return clientDetailsRepository.save(byClientId);


    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {

        ClientDetail clientDetails = clientDetailsRepository.findByClientId(clientId);

        if (null == clientDetails) {
            throw new NoSuchClientException("Client not found with ID '" + clientId + "'");
        }

        clientDetails.setClientSecret(passwordEncoder.encode(secret));

        clientDetailsRepository.save(clientDetails);

    }


    public ClientDetail resetClientSecret(String clientId, String secret) throws NoSuchClientException {

        ClientDetail clientDetails = clientDetailsRepository.findByClientId(clientId);

        if (null == clientDetails) {
            throw new NoSuchClientException("Client not found with ID '" + clientId + "'");
        }

        clientDetails.setClientSecret(passwordEncoder.encode(secret));

        return clientDetailsRepository.save(clientDetails);

    }


    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {

        ClientDetail clientDetails = clientDetailsRepository.findByClientId(clientId);

        if (null == clientDetails) {

            throw new NoSuchClientException("Client not found with ID '" + clientId + "'");

        }

        clientDetailsRepository.delete(clientDetails);

    }

    @Override
    public List<ClientDetails> listClientDetails() {

        List<ClientDetail> mdbcds = clientDetailsRepository.findAll();

        return getClientsFromMongoDBClientDetails(mdbcds);
    }


    public Collection<ClientDetail> findAllClientDetails() {
        return clientDetailsRepository.findAll();
    }

    private List<ClientDetails> getClientsFromMongoDBClientDetails(List<ClientDetail> clientDetails) {
        List<ClientDetails> baseClientDetails = new LinkedList<>();

        if (!CollectionUtils.isEmpty(clientDetails)) {

            clientDetails.stream().forEach(mdbcd -> baseClientDetails.add(getClientDetailsFromClientDetail(mdbcd)));

        }

        return baseClientDetails;
    }

    public ClientDetail save(ClientDetail authClient) {

        ClientDetail clientDetails = clientDetailsRepository.findByClientId(authClient.getClientId());

        if (null == clientDetails) {

            return clientDetailsRepository.save(authClient);


        }

        throw new ClientAlreadyExistsException("Client already exist with clientId '" + authClient.getClientId() + "' please choose another clientId");

    }

    public void deleteAll() {

        clientDetailsRepository.deleteAll();

    }
}
