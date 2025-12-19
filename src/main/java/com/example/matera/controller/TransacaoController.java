package com.example.matera.controller;

import com.example.matera.records.TransacaoRecord;
import com.example.matera.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contas")
@Validated
public class TransacaoController {

    private final ContaService service;

    public TransacaoController (ContaService service) {
        this.service = service;
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<Map<String, Object>> mostarSaldo(@PathVariable Long id) {
        BigDecimal saldo = service.getSaldo(id);
        return ResponseEntity.ok(Map.of("conta_id", id, "saldo", saldo));
    }

    @PostMapping("/{id}/transacao")
    public ResponseEntity<Void> enviarTransacao(
            @PathVariable Long id,
            @RequestBody @Valid List<TransacaoRecord> requests) {
        service.processTransactions(id, requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
