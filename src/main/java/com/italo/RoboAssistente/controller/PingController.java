package com.italo.RoboAssistente.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/ping")
    public String manterAcordado() {
        return "Robô operando 100%!";
    }
}