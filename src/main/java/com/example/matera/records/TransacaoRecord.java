package com.example.matera.records;

import com.example.matera.enums.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransacaoRecord (
        @NotNull TipoTransacao type,
        @NotNull @Positive BigDecimal valor,
        String descricao)
{ }
