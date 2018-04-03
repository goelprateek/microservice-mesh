package com.softcell.gonogo.cardstatementservice.service;

import com.softcell.gonogo.cardstatementservice.domain.CreditcardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

public interface CardService {

    String PREFIX = "/api";

    @RequestMapping(PREFIX+"/cards")
    ResponseEntity<Collection<CreditcardDto>> getCards();

    @RequestMapping(PREFIX+"/card/{cardId}")
    CreditcardDto getCard(@PathVariable("cardId") String cardId);

    @PostMapping(PREFIX+"/card")
    CreditcardDto save(@RequestBody CreditcardDto creditcardDto);
}
