package com.softcell.gonogo.cardstatementservice.rest;


import com.softcell.gonogo.cardstatementservice.client.CardClient;
import com.softcell.gonogo.cardstatementservice.client.StatementClient;
import com.softcell.gonogo.cardstatementservice.domain.CreditcardDto;
import com.softcell.gonogo.cardstatementservice.domain.StatementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CardStatementController {

    @Autowired
    private CardClient cardClient;

    @Autowired
    private StatementClient statementClient;


    @GetMapping("statement-by-card")
    public ResponseEntity<Map<CreditcardDto, Collection<StatementDto>>> getCardStatement(@RequestParam String cardId) {

        Map<CreditcardDto, Collection<StatementDto>> response = new HashMap<>();


        response.put(cardClient.getCard(cardId), Arrays.asList(statementClient.getStatement(cardId)));

        return ResponseEntity.ok(response);
    }

}
