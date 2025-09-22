### Laundry Automation — монорепозиторий микросервисов // Заголовок проекта

Минимальная рабочая основа на Scala 3 + ZIO 2 с многомодульной сборкой, миграциями Flyway и docker-compose для PostgreSQL/Adminer. Полные реализации сервисов, HTTP-эндпоинтов, моков и тестов будут добавляться поэтапно. // Краткое описание статуса

### Запуск инфраструктуры (Postgres + Adminer) // Раздел запуска БД

```bash
docker compose up -d # Поднимаем PostgreSQL и Adminer в фоне
docker compose ps # Проверяем состояние контейнеров
```

### Сборка и тесты (с покрытием) // Раздел команд SBT

```bash
sbt clean coverage test coverageReport coverageAggregate # Чистим, включаем покрытие, запускаем тесты и отчёт
```

### Сборка jar артефактов для сервисов // Раздел сборки jar

```bash
# Собрать все сервисы (создаст target/.../*.jar для каждого модуля)
sbt clean package # Сборка jar для всех подпроектов

# Примеры путей jar (после сборки):
# modules/api-gateway/target/scala-3.3.6/api-gateway_3-0.1.0-SNAPSHOT.jar # jar api-gateway
# modules/users-api/target/scala-3.3.6/users-api_3-0.1.0-SNAPSHOT.jar # jar users-api
```

### Переменные окружения (пример) // Раздел ENV

```bash
export APP_DB_URL="jdbc:postgresql://localhost:5432/laundry" # URL подключения к БД
export APP_DB_USER="laundry" # Пользователь БД
export APP_DB_PASSWORD="laundry" # Пароль БД
export MOCKS_ENABLED="true" # Включаем мок-режим интеграций по умолчанию
```

### Сборка Docker образов (пример, будет расширено) // Раздел Docker

```bash
# 1) Соберите jar: sbt clean package # Сборка всех модулей

# 2) Скопируйте jar в ожидаемые имена (упрощённо для локального теста образов)
cp modules/api-gateway/target/scala-3.3.6/*api-gateway*.jar target/scala-3.3.6/api-gateway.jar # Подготовка jar
cp modules/users-api/target/scala-3.3.6/*users-api*.jar target/scala-3.3.6/users-api.jar # Подготовка jar
# Аналогично для остальных сервисов при необходимости

# 3) Соберите образы
docker build -f docker/Dockerfile.api-gateway -t api-gateway:local . # Сборка образа api-gateway
docker build -f docker/Dockerfile.users-api -t users-api:local . # Сборка образа users-api

# 4) Запуск в MOCK-режиме (через docker compose)
docker compose up -d postgres adminer flyway # Поднимаем БД и миграции
# Затем поднимите нужные сервисы (после сборки их образов):
docker compose up -d api-gateway users-api # Поднимаем шлюз и users-api
```

### TODO / Открытые вопросы // Раздел вопросов

- Таймзона и время ежедневной рассылки отчётов. // Нужна фиксация TZ и расписания
- Поля и формат чеков OrangeData. // Требуется согласование формата
- Тип подключения Arduino и калибровка. // Определить транспорт и коэффициенты
- Полный список программ и цены. // Заполнить каталог программ


