package com.italo.RoboAssistente.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.italo.RoboAssistente.model.FundoImobiliario;
import com.italo.RoboAssistente.repository.FundoRepository;

@Service
public class AgendadorService {
    private static final Logger logger = LoggerFactory.getLogger(AgendadorService.class);
    private final B3Service b3Service;
    private final TelegramService telegramService;
    private final FundoRepository fundoRepository;
    private final CriptoService criptoService;
    public AgendadorService(B3Service b3Service, TelegramService telegramService, FundoRepository fundoRepository, CriptoService criptoService) {
        this.b3Service = b3Service;
        this.telegramService = telegramService;
        this.fundoRepository = fundoRepository;
        this.criptoService = criptoService;
    }
    @Scheduled(cron = "0 0/30 * * * MON-FRI")
    public void rotinaDeRelatorioFIIs() {
        logger.info("Iniciando varredura na B3 e nas Corretoras Cripto...");
        try {
            List<FundoImobiliario> carteira = fundoRepository.findAll();
            if (!carteira.isEmpty()) {
                String relatorioFIIs = b3Service.buscarOportunidades(carteira);
                if (!relatorioFIIs.isEmpty()) {
                    telegramService.enviarMensagem(relatorioFIIs);
                }
            }
String relatorioCripto = criptoService.buscarOportunidadeArbitragem();
            if (!relatorioCripto.isEmpty()) {
                telegramService.enviarMensagem(relatorioCripto);
            } else {
                logger.info("Cripto: O spread atual não cobre as taxas. Mantendo silêncio.");
            }
        } catch (Exception e) {
            logger.error("Falha critica ao executar a rotina de relatorio: {}", e.getMessage());
        }
    }
}