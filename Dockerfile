# --- STAGE 1: Build ---
# Usiamo un'immagine con Maven e JDK per compilare il progetto
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamo prima solo il pom.xml per sfruttare la cache dei layer di Docker
COPY pom.xml .
# Scarichiamo le dipendenze (questo step verrà cachato se il pom non cambia)
RUN mvn dependency:go-offline

# Copiamo il codice sorgente e compiliamo
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

# --- STAGE 2: Run ---
# Usiamo un'immagine JRE leggera (Alpine) solo per eseguire l'app
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiamo il file .jar generato nello stage precedente
COPY --from=build /app/target/*.jar app.jar

# Esponiamo la porta 8080
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]