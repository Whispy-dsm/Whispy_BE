FROM openjdk:21-jdk

RUN apt-get update && apt-get install -y findutils && rm -rf /var/lib/apt/lists/*

WORKDIR /app
ARG JAR_FILE=build/libs/*.jar

COPY . .

RUN if [ ! -f build/libs/*.jar ]; then \
        chmod +x ./gradlew && \
        ./gradlew clean build -x test --no-daemon; \
    fi

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

