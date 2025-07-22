FROM openjdk:21-jdk

WORKDIR /app
COPY . .

RUN ./gradlew clean build -x test --no-daemon
RUN cp build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]