# Estágio 1: Build (O motor vai baixar o Maven e compilar o seu código)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Run (Cria uma imagem super leve só com o Java para rodar o robô 24h)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copia o arquivo .jar compilado do Estágio 1
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# O comando que o servidor na nuvem vai rodar para ligar o seu sistema
ENTRYPOINT ["java", "-jar", "app.jar"]