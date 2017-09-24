package com.softcell.gonogo.statementservice.rest;

import com.softcell.gonogo.statementservice.domain.Statement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StatementController {

    private Collection<Statement> statements;

    @PostConstruct
    public void init(){
        statements = new ArrayList<>();

        statements.add(Statement.builder().cardId("1").id("1").operationDate("11/20").value("10000").build());

    }

    @GetMapping("/statement")
    public ResponseEntity<Collection<Statement>> getStatements(){
        return ResponseEntity.ok(statements);
    }

    @GetMapping("/statement/{statementId}")
    public ResponseEntity<Statement> getStatement(@PathVariable String statementId){
        return ResponseEntity.ok(Optional.of(statements.stream().filter((statement) -> statement.getId().equalsIgnoreCase(statementId)).reduce(null, (u,v) -> {
            if (u != null && v != null)
                throw new IllegalStateException("More than one StatementId found");
            else return u == null ? v : u;
        })).get());
    }

    @PostMapping("/new-statement")
    public ResponseEntity<Statement> save(@Valid @RequestBody Statement statement){

        if(null != statement && statement.getId() != null){
            statements.add(statement);

        }

        return ResponseEntity.ok(statement);
    }

}
