# Estágio 1: Compilação com Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Imagem final de execução
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia o .jar compilado do estágio anterior
COPY --from=build /app/target/*.jar app.jar
# Expõe a porta que sua aplicação usa (ex: 8080)
EXPOSE 8080
# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]