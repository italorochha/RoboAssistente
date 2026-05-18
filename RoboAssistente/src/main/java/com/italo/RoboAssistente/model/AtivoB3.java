package com.italo.RoboAssistente.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AtivoB3(
    String symbol,
    double regularMarketPrice,
    double regularMarketPreviousClose
) {
}