package com.softcell.gonogo.uaaserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by prateek on 24/5/17.
 */
@Document(collection = "oauth2_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDetail  {

    @Id
    private String id;

    @Indexed
    @Valid
    private String clientId;

    @Valid
    @NotNull
    private Set<String> resourceIds;

    private boolean secretRequired;

    @Indexed
    private String clientSecret;

    private boolean scoped;

    @Valid
    @NotNull
    private Set<String> scope;

    @Valid
    @NotNull
    private Set<String> authorizedGrantTypes;

    @Valid
    @NotNull
    private Set<String> registeredRedirectUri;

    @Valid
    @NotNull
    private Collection<String> authorities;

    @Valid
    private Integer accessTokenValiditySeconds;

    @Valid
    private Integer refreshTokenValiditySeconds;


    private boolean autoApprove;

    private Map<String, Object> additionalInformation;


}
