# STAGE 1: extract layers
FROM eclipse-temurin:25-jre-alpine AS builder
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

COPY docker/start-service.sh ./

RUN java -Djarmode=tools -jar app.jar extract --layers --destination /app/layers

# STAGE 2: runtime
FROM eclipse-temurin:25-jre-alpine
RUN apk add --no-cache libgcc libstdc++

WORKDIR /usr/app

RUN addgroup -S app && adduser -S app -G app
USER app:app

COPY --from=builder /app/layers/dependencies/ ./
COPY --from=builder /app/layers/snapshot-dependencies/ ./
COPY --from=builder /app/layers/spring-boot-loader/ ./
COPY --from=builder /app/layers/application/ ./
COPY --from=builder /app/start-service.sh ./


CMD ./start-service.sh