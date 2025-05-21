FROM mcr.microsoft.com/playwright/java:v1.50.0-jammy

WORKDIR /app

# Обновляем сертификаты и устанавливаем Maven
RUN apt-get update && \
    apt-get install -y ca-certificates maven && \
    update-ca-certificates

# Копируем проектные файлы
COPY pom.xml ./
COPY src ./src

# Выполняем сборку (отдельно для диагностики)
RUN mvn clean install

CMD ["mvn", "test", "-Dtest=DragDropTest"]