package com.example.matera.service;

import com.example.matera.enums.TipoTransacao;
import com.example.matera.model.Conta;
import com.example.matera.records.TransacaoRecord;
import com.example.matera.repository.ContaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository repository;

    public ContaService(ContaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BigDecimal getSaldo(Long id) {
        return repository.findById(id)
                .map(Conta::getSaldo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));
    }

    @Transactional
    public void processTransactions(Long id, List<TransacaoRecord> requests) {
        Conta conta = repository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        for (TransacaoRecord req : requests) {
            if (req.type() == TipoTransacao.DEBIT) {
                if (conta.getSaldo().compareTo(req.valor()) < 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Saldo insuficiente para o débito");
                }
                conta.setSaldo(conta.getSaldo().subtract(req.valor()));
            } else {
                conta.setSaldo(conta.getSaldo().add(req.valor()));
            }
        }
        repository.save(conta);
    }

}
