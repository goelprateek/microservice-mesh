package com.softcell.gonogo.uaaserver.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "oauth2_user")
@Data
@Builder
public class User {

    @Id
    private String id;
    private String email;
    private String password;
    private List<String> rights;
    @CreatedDate
    private Date dateCreated;
    private Boolean isActivated;
}
