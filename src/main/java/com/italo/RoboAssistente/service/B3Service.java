package com.italo.RoboAssistente.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.italo.RoboAssistente.model.AtivoB3;
import com.italo.RoboAssistente.model.BrapiResponse;
import com.italo.RoboAssistente.model.FundoImobiliario;

@Service
public class B3Service {

    @Value("${brapi.token}")
    private String brapiToken;
    @Value("${brapi.url}")
    private String brapiUrl;
    private final ObjectMapper objectMapper;
    public B3Service(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    public String buscarOportunidades(List<FundoImobiliario> carteira) {
        StringBuilder mensagemFinal = new StringBuilder();
        boolean encontrouOportunidade = false;
        HttpClient client = HttpClient.newHttpClient();
        for (FundoImobiliario fundo : carteira) {
            try {
                String urlCompleta = brapiUrl + "/" + fundo.getTicker().trim() + "?token=" + brapiToken;
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(urlCompleta))
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    BrapiResponse dados = objectMapper.readValue(response.body(), BrapiResponse.class);
                    AtivoB3 ativo = dados.results().get(0);
                    double precoAtual = ativo.regularMarketPrice();
                    double precoTeto = fundo.getPrecoTeto();
                    if (precoAtual <= precoTeto) {
                        if (!encontrouOportunidade) {
                            mensagemFinal.append(" *ALERTA DE OPORTUNIDADE (FIIs)* \n\n");
                            encontrouOportunidade = true;
                        }
                        double precoAnterior = ativo.regularMarketPreviousClose();
                        double diferencaReais = precoAtual - precoAnterior;
                        double diferencaPorcentagem = (diferencaReais / precoAnterior) * 100;
                        String icone = diferencaReais >= 0 ? "📈" : "📉";
                        mensagemFinal.append(String.format(" *%s* entrou em ponto de compra!\n", fundo.getTicker()));
                        mensagemFinal.append(String.format(" Atual: R$ %.2f\n", precoAtual));
                        mensagemFinal.append(String.format(" Teto: R$ %.2f\n", precoTeto));
                        mensagemFinal.append(String.format("%s Variação: R$ %.2f (%.2f%%)\n", icone, diferencaReais, diferencaPorcentagem));
                        mensagemFinal.append("-------------------------\n");
                    }
                }
            } catch (IOException | InterruptedException e) {
            }
        }
        return encontrouOportunidade ? mensagemFinal.toString() : "";
    }
}