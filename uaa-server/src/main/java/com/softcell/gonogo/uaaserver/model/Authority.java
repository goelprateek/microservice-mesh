package com.softcell.gonogo.uaaserver.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


/**
 * An authority (a security role) used by Spring Security.
 */
@Data
@EqualsAndHashCode
@ToString
@Document(collection = "oauth2_authority")
public class Authority implements Serializable {

    @NotNull
    @Size(min = 0, max = 50)
    @Id
    private String name;

}
