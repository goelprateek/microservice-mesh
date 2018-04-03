package com.softcell.gonogo.cardstatementservice.service;

import com.softcell.gonogo.cardstatementservice.domain.StatementDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface StatementService {

    String PREFIX = "/api";

    @GetMapping(PREFIX+"/statements")
    StatementDto getAllStatements();

    @GetMapping(PREFIX+"/statement/{cardId}")
    StatementDto getStatement(@PathVariable("cardId") String cardId);

    @PostMapping(PREFIX+"/statement")
    StatementDto save(@RequestBody StatementDto statementDto);

}
