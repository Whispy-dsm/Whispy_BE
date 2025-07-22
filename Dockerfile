FROM openjdk:21-jdk

WORKDIR /app
COPY . .

# JAR 파일이 없을 때만 빌드
RUN if [ ! -f build/libs/*.jar ]; then \
        chmod +x ./gradlew && \
        ./gradlew clean build -x test --no-daemon; \
    fi

RUN cp build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]