package com.italo.RoboAssistente.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.italo.RoboAssistente.service.IaService;

@RestController
public class PingController {
    @Autowired
    private IaService iaService;
    @GetMapping("/ping")
    public String manterAcordado() {
        return "Robô operando 100%!";
    }
    @GetMapping("/resumo")
    public String testarInteligencia() {
        String manchetes = "Ibovespa bate recorde histórico puxado por ações da Petrobras. Dólar opera em forte queda após dados de inflação nos EUA. Bitcoin ultrapassa barreira de resistência e anima investidores.";
        String analise = iaService.analisarSentimento(manchetes);
        return analise;
    }
}