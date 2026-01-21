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

# Argomenti di build (da passare con --build-arg)
ARG DB_URL
ARG DB_USER
ARG DB_PASS
ARG MAIL
ARG PASSWORD_MAIL

# Impostiamo le variabili d'ambiente in modo che l'applicazione le veda
ENV DB_URL=${DB_URL}
ENV DB_USER=${DB_USER}
ENV DB_PASS=${DB_PASS}
ENV MAIL=${MAIL}
ENV PASSWORD_MAIL=${PASSWORD_MAIL}

# Copiamo il file .jar generato nello stage precedente
COPY --from=build /app/target/*.jar app.jar

# Esponiamo la porta 80
EXPOSE 80

# Comando di avvio
ENTRYPOINT ["java", "-jar", "app.jar"]