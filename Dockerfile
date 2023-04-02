FROM openjdk:11-jre-slim

COPY target/phexpedition-app-jar-with-dependencies.jar /app/phexpedition-app.jar

WORKDIR /app
EXPOSE 8080

CMD ["java", "-jar", "phexpedition-app.jar"]
