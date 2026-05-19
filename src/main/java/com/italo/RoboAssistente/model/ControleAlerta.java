package com.italo.RoboAssistente.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "controle_alertas")
public class ControleAlerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoAlerta;
    private LocalDate dataEnvio;
    public ControleAlerta() {}
    public ControleAlerta(String tipoAlerta, LocalDate dataEnvio) {
        this.tipoAlerta = tipoAlerta;
        this.dataEnvio = dataEnvio;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }
    public LocalDate getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDate dataEnvio) { this.dataEnvio = dataEnvio; }
}