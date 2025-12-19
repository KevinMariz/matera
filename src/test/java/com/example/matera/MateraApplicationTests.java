package com.example.matera;

import com.example.matera.enums.TipoTransacao;
import com.example.matera.records.TransacaoRecord;
import com.example.matera.repository.ContaRepository;
import com.example.matera.service.ContaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MateraApplicationTests {
    @Autowired
    private ContaService service;
    @Autowired
    private ContaRepository repository;

    @Test
    void testeConcorrenciaTransacao() throws InterruptedException {
        Long conta_id = 1L;
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    service.processTransactions(conta_id, List.of(
                            new TransacaoRecord(TipoTransacao.DEBIT, new BigDecimal("10.00"), "Teste")
                    ));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        BigDecimal saldoFinal = service.getSaldo(conta_id);

        // Se thread-safe, 100 - (10 * 10) deve ser 0
        assertEquals(0, saldoFinal.compareTo(BigDecimal.ZERO));
    }

}
