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
    private final IaService iaService;
    private final NoticiasService noticiasService;
    private final TesouroDiretoService tesouroDiretoService;
    public AgendadorService(B3Service b3Service, TelegramService telegramService, FundoRepository fundoRepository, CriptoService criptoService, IaService iaService, NoticiasService noticiasService, TesouroDiretoService tesouroDiretoService) {
        this.b3Service = b3Service;
        this.telegramService = telegramService;
        this.fundoRepository = fundoRepository;
        this.criptoService = criptoService;
        this.iaService = iaService;
        this.noticiasService = noticiasService;
        this.tesouroDiretoService = tesouroDiretoService;
    }
    @Scheduled(cron = "0 0/30 10-17 * * *", zone = "America/Sao_Paulo")
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
    String manchetesDoDia = noticiasService.buscarManchetesDoDia();
    String analiseIA = iaService.analisarSentimento(manchetesDoDia);
    String alertaTesouro = tesouroDiretoService.verificarCurvaDeJuros();
    String mensagemFinal = alertaTesouro + "\n\n"
    + " *Visão de Mercado (IA):*\n"
    + analiseIA + "\n\n"
    + " *Cotações de Hoje:*\n"
    + relatorioCripto;

    telegramService.enviarMensagem(mensagemFinal);
        } catch (Exception e) {
            logger.error("Falha critica ao executar a rotina de relatorio: {}", e.getMessage());
        }
    }
}