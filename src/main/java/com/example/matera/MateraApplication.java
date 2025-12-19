package com.example.matera;

import com.example.matera.model.Conta;
import com.example.matera.repository.ContaRepository;
import com.example.matera.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MateraApplication {

    public static void main(String[] args) {
        SpringApplication.run(MateraApplication.class, args);
    }

}
