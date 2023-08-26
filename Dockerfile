# Используем базовый образ с Java
FROM amazoncorretto:17.0.7-alpine

# Создаем директорию app внутри Docker-образа
WORKDIR /app

# Копируем собранный .jar файл внутрь Docker-образа
COPY ./build/libs/ScheduleWeb-0.0.1-SNAPSHOT.jar /app/app.jar

# Устанавливаем команду, которая будет выполняться при запуске Docker-контейнера
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
