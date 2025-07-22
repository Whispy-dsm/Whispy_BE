FROM openjdk:21-jdk

WORKDIR /app
ARG JAR_FILE=build/libs/*.jar

COPY . .

RUN if [ ! -f ${JAR_FILE} ]; then \
        chmod +x ./gradlew && \
        ./gradlew clean build -x test --no-daemon; \
    fi

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]