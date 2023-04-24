FROM openjdk:17-slim
EXPOSE 8080:8080
RUN mkdir /app
#COPY --from=build /home/maven/src/target/*-with-dependencies.jar /app/ktor-docker-sample.jar
COPY target/phexpedition-app-jar-with-dependencies.jar /app/phexpedition.jar
ENTRYPOINT ["java","-jar","/app/phexpedition.jar"]