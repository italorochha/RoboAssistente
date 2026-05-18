# RoboAssistente Financeiro (Backend)

Sistema de backend autônomo desenvolvido em Java com Spring Boot para monitoramento inteligente de ativos do mercado financeiro e envio de alertas via Telegram. O sistema atua como um worker/daemon, realizando varreduras periódicas em APIs externas e aplicando lógicas de negócios para identificar oportunidades de compra.

## Arquitetura e Funcionalidades

O projeto é dividido em dois motores principais de análise:

1.  **Monitoramento de FIIs (Fundos Imobiliários):**
* Integração com a B3 via API Brapi.
* Utilização de Banco de Dados em memória (H2) via Spring Data JPA para gerenciamento da carteira e definição de "Preço Teto".
* Cálculo de variação percentual em relação ao fechamento anterior.
* Filtro inteligente: Alertas são disparados apenas se o preço de mercado atual for menor ou igual ao Preço Teto estipulado no banco de dados.

2.  **Scanner de Arbitragem (Criptomoedas):**
* Consumo simultâneo de dados de múltiplas exchanges (Binance e KuCoin) utilizando o moderno `HttpClient` nativo do Java.
* Conversão e mapeamento de dados via Jackson (`ObjectMapper`).
* Cálculo de Spread (Lucro Bruto) e aplicação de regras de negócio com custos operacionais (Taxas Maker/Taker e Taxa de Rede).
* Filtro inteligente: O sistema entra em silêncio (omissão de notificação) caso o lucro líquido estimado não cubra os custos da operação.

## Tecnologias Utilizadas

* **Java 17** (Records, HttpClient, var)
* **Spring Boot 3.x**
* **Spring Web** (REST API consumption)
* **Spring Data JPA / Hibernate** (ORM)
* **H2 Database** (In-memory DB)
* **Spring Scheduling** (@Scheduled / Cron Expressions)
* **SLF4J / Logback** (Padronização de logs corporativos)

## Pré-requisitos

Para rodar o projeto localmente, é necessário ter instalado:
* Java Development Kit (JDK) 17 ou superior.
* Maven.
* Um bot configurado no Telegram (Token e Chat ID).
* Um token gratuito na plataforma Brapi.dev.

## Configuração do Ambiente

1. Clone o repositório.
2. Navegue até o diretório `src/main/resources` e abra o arquivo `application.properties`.
3. Insira as suas credenciais de ambiente:

```properties
telegram.bot.token=SEU_TOKEN_DO_TELEGRAM
telegram.chat.id=SEU_CHAT_ID

brapi.token=SEU_TOKEN_DA_BRAPI
brapi.url=[https://brapi.dev/api/quote](https://brapi.dev/api/quote)