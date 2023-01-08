FROM amazoncorretto:19-alpine-jdk
EXPOSE 9090
ADD ./target/telegram-weather-api-0.0.1-SNAPSHOT.jar telegram-weather-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/telegram-weather-api-0.0.1-SNAPSHOT.jar"]