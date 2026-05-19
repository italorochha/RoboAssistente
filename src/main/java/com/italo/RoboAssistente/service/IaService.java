package com.italo.RoboAssistente.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class IaService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    public String analisarSentimento(String manchetes) {
        String urlCompleta = geminiApiUrl + geminiApiKey;
        String prompt = "Atue como um analista financeiro sênior. Leia as seguintes manchetes do dia e forneça um resumo de no máximo 3 linhas dizendo se o sentimento do mercado é de Otimismo, Pessimismo ou Neutro. Seja direto ao ponto. Manchetes: " + manchetes;

        String corpoRequisicao = "{ \"contents\": [{ \"parts\": [{\"text\": \"" + prompt + "\"}] }] }";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(corpoRequisicao, headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(urlCompleta, requestEntity, String.class);
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Erro ao conectar com a IA: " + e.getMessage());
            return "Erro ao analisar o sentimento do mercado hoje.";
        }
    }
}