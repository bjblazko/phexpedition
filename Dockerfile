FROM openjdk:17-slim

EXPOSE 8080:8080

RUN mkdir /app

COPY target/phexpedition-app-runner.jar /app/phexpedition.jar

EXPOSE 8080
USER 185
ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

ENTRYPOINT ["java","-jar","/app/phexpedition.jar"]