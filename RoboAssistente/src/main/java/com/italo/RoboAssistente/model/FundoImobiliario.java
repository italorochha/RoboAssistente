package com.italo.RoboAssistente.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FundoImobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ticker;
    private double precoTeto;
    public FundoImobiliario() {
    }
    public FundoImobiliario(String ticker, double precoTeto) {
        this.ticker = ticker;
        this.precoTeto = precoTeto;
    }
    public Long getId() { return id; }
    public String getTicker() { return ticker; }
    public double getPrecoTeto() { return precoTeto; }
}