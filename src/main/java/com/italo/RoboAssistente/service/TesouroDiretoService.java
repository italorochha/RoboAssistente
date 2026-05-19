package com.italo.RoboAssistente.service;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.italo.RoboAssistente.model.ControleAlerta;
import com.italo.RoboAssistente.repository.ControleAlertaRepository;

@Service
public class TesouroDiretoService {
    private final ControleAlertaRepository controleRepository;
    public TesouroDiretoService(ControleAlertaRepository controleRepository) {
        this.controleRepository = controleRepository;
    }
    public String verificarCurvaDeJuros() {
        String url = "https://www.tesourodireto.com.br/json/br/com/b3/tesourodireto/service/api/treasurybondsinfo.json";
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode listaTitulos = root.path("response").path("TrsrBdTradgList");
            for (JsonNode tituloNode : listaTitulos) {
                JsonNode infoTitulo = tituloNode.path("TrsrBd");
                String nome = infoTitulo.path("nm").asText();
                if (nome.contains("Tesouro IPCA+ 2035")) {
                    double taxa = infoTitulo.path("anulInvstmtRate").asDouble();
                    if (taxa >= 6.0) {
                        LocalDate hoje = LocalDate.now();
                        boolean jaEnviou = controleRepository.existsByTipoAlertaAndDataEnvio("TESOURO_IPCA_ALTO", hoje);
                        if (!jaEnviou) {
                            controleRepository.save(new ControleAlerta("TESOURO_IPCA_ALTO", hoje));
                            return " *ALERTA MACROECONÔMICO:*\nA curva de juros abriu forte! O Tesouro IPCA+ 2035 está a pagar IPCA + " + taxa + "% ao ano.\nHistoricamente, os FIIs sofrem descontos gigantescos nestes cenários. É a janela ideal para ir às compras!";
                        } else {
                            return " *Radar Macro:* O Tesouro IPCA+ 2035 continua em zona de oportunidade (IPCA + " + taxa + "%). Você já foi alertado hoje.";
                        }
                    } else {
                        return " *Radar Macro:* O Tesouro IPCA+ 2035 está controlado em IPCA + " + taxa + "%. Mercado sem anomalias de preço para os FIIs hoje.";
                    }
                }
            }
            return "";
        } catch (JsonProcessingException | RestClientException e) {
            System.err.println("Erro ao buscar dados do Tesouro Direto: " + e.getMessage());
            return "";
        }
    }
}