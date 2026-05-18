package com.italo.RoboAssistente;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.italo.RoboAssistente.model.FundoImobiliario;
import com.italo.RoboAssistente.repository.FundoRepository;

@SpringBootApplication
@EnableScheduling
public class RoboAssistenteApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoboAssistenteApplication.class, args);
    }
    @Bean
    public CommandLineRunner popularBanco(FundoRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new FundoImobiliario("MXRF11", 10.00));
                repository.save(new FundoImobiliario("HGLG11", 160.00));
                repository.save(new FundoImobiliario("KNCR11", 105.00));
                repository.save(new FundoImobiliario("BTLG11", 100.00));
                System.out.println(" Banco de dados populado com a sua carteira de FIIs!");
            }
        };
    }
}
