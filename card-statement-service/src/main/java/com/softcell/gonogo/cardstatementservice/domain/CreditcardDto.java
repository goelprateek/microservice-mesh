package com.softcell.gonogo.cardstatementservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CreditcardDto {

    private String cardId;
    private String cardHolderName;
    private String pan ;
    private  String validate;



}
