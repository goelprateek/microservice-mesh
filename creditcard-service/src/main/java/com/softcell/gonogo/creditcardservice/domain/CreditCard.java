package com.softcell.gonogo.creditcardservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreditCard {

    private String cardId;
    private String cardHolderName;
    private String pan ;
    private  String validate;


}
