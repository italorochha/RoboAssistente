package com.italo.RoboAssistente.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

// Ignora qualquer dado do JSON que não mapearmos aqui (como logomarca, horário, etc)
@JsonIgnoreProperties(ignoreUnknown = true)
public record BrapiResponse(List<AtivoB3> results) {
}