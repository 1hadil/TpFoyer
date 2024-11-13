# Utiliser une image légère OpenJDK basée sur Alpine Linux
FROM openjdk:17-jdk-alpine

# Exposer le port sur lequel l'application Spring Boot sera disponible
EXPOSE 8089

# Ajouter votre fichier JAR généré dans le conteneur
COPY target/tp-foyer-5.0.0.jar /tp-foyer-5.0.0.jar

# Définir le point d'entrée pour exécuter l'application avec Java
ENTRYPOINT ["java", "-jar", "/tp-foyer-5.0.0.jar"]
