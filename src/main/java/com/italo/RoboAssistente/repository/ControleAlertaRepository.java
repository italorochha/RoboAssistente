package com.italo.RoboAssistente.repository;

import com.italo.RoboAssistente.model.ControleAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

@Repository
public interface ControleAlertaRepository extends JpaRepository<ControleAlerta, Long> {
    boolean existsByTipoAlertaAndDataEnvio(String tipoAlerta, LocalDate dataEnvio);
}