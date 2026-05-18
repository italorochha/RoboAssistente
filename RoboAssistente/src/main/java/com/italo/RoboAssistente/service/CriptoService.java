package com.italo.RoboAssistente.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.italo.RoboAssistente.model.BinanceResponse;
import com.italo.RoboAssistente.model.KuCoinResponse;

@Service
public class CriptoService {

    private final ObjectMapper objectMapper;
    private final HttpClient client;

    public CriptoService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.client = HttpClient.newHttpClient();
    }
    public String buscarOportunidadeArbitragem() {
        try {
            HttpRequest requestBinance = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT"))
                    .GET().build();
            HttpResponse<String> responseBinance = client.send(requestBinance, HttpResponse.BodyHandlers.ofString());
            BinanceResponse binance = objectMapper.readValue(responseBinance.body(), BinanceResponse.class);
            double precoBinance = Double.parseDouble(binance.price());
            HttpRequest requestKuCoin = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.kucoin.com/api/v1/market/orderbook/level1?symbol=BTC-USDT"))
                    .GET().build();
            HttpResponse<String> responseKuCoin = client.send(requestKuCoin, HttpResponse.BodyHandlers.ofString());
            KuCoinResponse kuCoin = objectMapper.readValue(responseKuCoin.body(), KuCoinResponse.class);
            double precoKuCoin = Double.parseDouble(kuCoin.data().price());
            double precoCompra;
            double precoVenda;
            String corretoraCompra;
            String corretoraVenda;
            if (precoBinance < precoKuCoin) {
                precoCompra = precoBinance;
                corretoraCompra = "Binance";
                precoVenda = precoKuCoin;
                corretoraVenda = "KuCoin";
            } else {
                precoCompra = precoKuCoin;
                corretoraCompra = "KuCoin";
                precoVenda = precoBinance;
                corretoraVenda = "Binance";
            }
            double lucroBruto = precoVenda - precoCompra;
            double taxaCompra = precoCompra * 0.001;
            double taxaVenda = precoVenda * 0.001;
            double taxaTransferencia = 15.00;
            double custoTotalTaxas = taxaCompra + taxaVenda + taxaTransferencia;
            double lucroLiquido = lucroBruto - custoTotalTaxas;

            if (lucroLiquido > 0) {
                StringBuilder mensagem = new StringBuilder();
                mensagem.append(" *OPORTUNIDADE REAL (Arbitragem)* ⚡\n\n");
                mensagem.append(" Ativo: Bitcoin (BTC/USDT)\n");
                mensagem.append(String.format(" Comprar na %s: $ %.2f\n", corretoraCompra, precoCompra));
                mensagem.append(String.format(" Vender na %s: $ %.2f\n", corretoraVenda, precoVenda));
                mensagem.append("-------------------------\n");
                mensagem.append(String.format(" Lucro Bruto: $ %.2f\n", lucroBruto));
                mensagem.append(String.format(" Custo de Taxas: $ %.2f\n", custoTotalTaxas));
                mensagem.append(String.format(" *LUCRO LÍQUIDO:* $ %.2f por BTC\n", lucroLiquido));

                return mensagem.toString();
            } else {
                return "";
            }
        } catch (IOException | InterruptedException | NumberFormatException e) {
            System.out.println("Erro ao cotar criptomoedas: " + e.getMessage());
            return "";
        }
    }
}