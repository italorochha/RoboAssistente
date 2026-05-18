package com.italo.RoboAssistente.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KuCoinResponse(KuCoinData data) {
}