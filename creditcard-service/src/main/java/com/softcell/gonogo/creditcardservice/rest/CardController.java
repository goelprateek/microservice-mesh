package com.softcell.gonogo.creditcardservice.rest;

import com.softcell.gonogo.creditcardservice.domain.CreditCard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CardController {

    private Collection<CreditCard>  creditCards;


    @PostConstruct
    public void init(){

        creditCards = new ArrayList<>();

        creditCards.add(CreditCard.builder().cardId("1").cardHolderName("prateek").pan("alvpg7356b").validate("11/20").build());

    }


    @GetMapping("/cards")
    public Collection<CreditCard> getCards(){
        return creditCards;
    }

    @GetMapping("/card/{cardId}")
    public CreditCard getCreditCard(@PathVariable("cardId") String cardId){
        return Optional.of(creditCards.stream().filter((card) -> card.getCardId().equalsIgnoreCase(cardId)).reduce(null , (u,v) -> {
            if (u != null && v != null)
                throw new IllegalStateException("More than one CardId found");
            else return u == null ? v : u;
        })).get();
    }

    @PostMapping("/new-card")
    public CreditCard saveCard(@RequestBody CreditCard creditCard){

        if(null != creditCard && creditCard.getCardId() != null){
            creditCards.add(creditCard);
        }

        return creditCard;
    }

}
