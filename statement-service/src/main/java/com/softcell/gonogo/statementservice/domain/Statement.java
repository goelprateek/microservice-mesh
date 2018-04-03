package com.softcell.gonogo.statementservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Statement {

    private String id;
    private String cardId;
    private String operationDate;
    private String value;


}
