FROM eclipse-temurin:21.0.2_13-jre-alpine
VOLUME /tmp
COPY build/libs/ersms-users.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]