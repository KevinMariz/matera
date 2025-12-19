package com.example.matera.repository;

import com.example.matera.model.Conta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Conta a WHERE a.id = :id")
    Optional<Conta> findByIdForUpdate(Long id);
}
