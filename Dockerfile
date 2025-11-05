FROM eclipse-temurin:21-jdk

WORKDIR /app
COPY . .

RUN if [ ! -f build/libs/*.jar ]; then \
        chmod +x ./gradlew && \
        ./gradlew clean build -x test --no-daemon; \
    fi && \
    JAR_FILE=$(ls build/libs/*.jar | head -1) && \
    cp "$JAR_FILE" app.jar

HEALTHCHECK --interval=30s --timeout=10s --start-period=15s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]