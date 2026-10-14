FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

RUN useradd --system --uid 10001 --create-home appuser

COPY bank.jar app.jar

EXPOSE 8080

USER appuser

ENTRYPOINT ["java", "-jar", "app.jar"]
