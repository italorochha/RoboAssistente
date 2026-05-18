package com.italo.RoboAssistente.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TelegramService {
    @Value("${telegram.bot.token}")
    private String botToken;
    @Value("${telegram.chat.id}")
    private String chatId;
    public void enviarMensagem(String texto) {
        try {
            String textoCodificado = URLEncoder.encode(texto, StandardCharsets.UTF_8);
            String url = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chatId + "&text=" + textoCodificado;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Disparo para o Telegram concluído. Status da resposta: " + response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro crítico ao tentar conectar com a internet: " + e.getMessage());
        }
    }
}