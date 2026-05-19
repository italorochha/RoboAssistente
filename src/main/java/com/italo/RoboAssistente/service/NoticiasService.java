package com.italo.RoboAssistente.service;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class NoticiasService {
    public String buscarManchetesDoDia() {
        StringBuilder manchetes = new StringBuilder();
        try {
            String url = "https://g1.globo.com/economia/";
            Document doc = Jsoup.connect(url).get();
            Elements titulos = doc.select(".feed-post-link");
            int limite = Math.min(titulos.size(), 3);
            for (int i = 0; i < limite; i++) {
                Element materia = titulos.get(i);
                manchetes.append("- ").append(materia.text()).append("\n");
            }
            if (manchetes.isEmpty()) {
                return "Sem notícias de grande impacto no momento.";
            }
            return manchetes.toString();
        } catch (IOException e) {
            System.err.println("Erro ao extrair notícias: " + e.getMessage());
            return "Não foi possível carregar as notícias financeiras de hoje.";
        }
    }
}