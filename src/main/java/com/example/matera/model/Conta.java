package com.example.matera.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @PositiveOrZero
    private BigDecimal saldo = BigDecimal.ZERO;

    public Conta() {}

    public Conta(BigDecimal saldo) {
        this.saldo = saldo;
    }

}
