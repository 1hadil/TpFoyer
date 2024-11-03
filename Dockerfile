# Utilisez l'image de base OpenJDK 17 Alpine
FROM openjdk:17-jdk-alpine

# Exposer le port de l'application
EXPOSE 8089

# Copier tous les fichiers du projet dans le conteneur
COPY . /app

# Spécifiez le répertoire de travail
WORKDIR /app

# Ajoutez le JAR de votre application
ADD target/tp-foyer-5.0.0.jar tp-foyer-5.0.0.jar

# Définir le point d'entrée pour exécuter l'application
ENTRYPOINT ["java", "-jar", "tp-foyer-5.0.0.jar"]
