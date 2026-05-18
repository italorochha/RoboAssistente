package com.italo.RoboAssistente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.italo.RoboAssistente.model.FundoImobiliario;
public interface FundoRepository extends JpaRepository<FundoImobiliario, Long> {
}