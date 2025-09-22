### Laundry Automation — монорепозиторий микросервисов // Заголовок проекта

Минимальная рабочая основа на Scala 3 + ZIO 2 с многомодульной сборкой, миграциями Flyway и docker-compose для PostgreSQL/Adminer. Полные реализации сервисов, HTTP-эндпоинтов, моков и тестов будут добавляться поэтапно. // Краткое описание статуса

### Запуск инфраструктуры (Postgres + Adminer) // Раздел запуска БД

```bash
docker compose up -d # Поднимаем PostgreSQL и Adminer в фоне
docker compose ps # Проверяем состояние контейнеров
```

### Полный MOCK e2e запуск // Раздел e2e

```bash
# 1) Соберите все модули
sbt clean package # Собираем jar для всех подпроектов

# 2) Подготовьте jar для образов (упрощённо)
cp modules/api-gateway/target/scala-3.3.6/*api-gateway*.jar target/scala-3.3.6/api-gateway.jar # Готовим jar
cp modules/users-api/target/scala-3.3.6/*users-api*.jar target/scala-3.3.6/users-api.jar # Готовим jar
cp modules/admin-api/target/scala-3.3.6/*admin-api*.jar target/scala-3.3.6/admin-api.jar # Готовим jar
cp modules/machines-svc/target/scala-3.3.6/*machines-svc*.jar target/scala-3.3.6/machines-svc.jar # Готовим jar
cp modules/lg-adapter/target/scala-3.3.6/*lg-adapter*.jar target/scala-3.3.6/lg-adapter.jar # Готовим jar
cp modules/doser-gateway/target/scala-3.3.6/*doser-gateway*.jar target/scala-3.3.6/doser-gateway.jar # Готовим jar
cp modules/payments-svc/target/scala-3.3.6/*payments-svc*.jar target/scala-3.3.6/payments-svc.jar # Готовим jar
cp modules/fiscal-svc/target/scala-3.3.6/*fiscal-svc*.jar target/scala-3.3.6/fiscal-svc.jar # Готовим jar
cp modules/reporting-svc/target/scala-3.3.6/*reporting-svc*.jar target/scala-3.3.6/reporting-svc.jar # Готовим jar
cp modules/email-svc/target/scala-3.3.6/*email-svc*.jar target/scala-3.3.6/email-svc.jar # Готовим jar

# 3) Соберите образы
docker build -f docker/Dockerfile.api-gateway -t api-gateway:local . # Сборка api-gateway
docker build -f docker/Dockerfile.users-api -t users-api:local . # Сборка users-api
docker build -f docker/Dockerfile.admin-api -t admin-api:local . # Сборка admin-api
docker build -f docker/Dockerfile.machines-svc -t machines-svc:local . # Сборка machines-svc
docker build -f docker/Dockerfile.lg-adapter -t lg-adapter:local . # Сборка lg-adapter
docker build -f docker/Dockerfile.doser-gateway -t doser-gateway:local . # Сборка doser-gateway
docker build -f docker/Dockerfile.payments-svc -t payments-svc:local . # Сборка payments-svc
docker build -f docker/Dockerfile.fiscal-svc -t fiscal-svc:local . # Сборка fiscal-svc
docker build -f docker/Dockerfile.reporting-svc -t reporting-svc:local . # Сборка reporting-svc
docker build -f docker/Dockerfile.email-svc -t email-svc:local . # Сборка email-svc

# 4) Запустите инфраструктуру и миграции
docker compose up -d postgres adminer flyway # Поднимаем БД и применяем миграции

# 5) Запустите сервисы в MOCK-режиме
docker compose up -d api-gateway users-api admin-api machines-svc lg-adapter doser-gateway payments-svc fiscal-svc reporting-svc email-svc # Поднимаем все сервисы
```

### Сборка и тесты (с покрытием) // Раздел команд SBT

```bash
sbt clean coverage test coverageReport coverageAggregate # Чистим, включаем покрытие, запускаем тесты и отчёт
```

Минимальная цель покрытия 80%+ уже настроена в `build.sbt` через scoverage и сборка упадёт при меньшем покрытии. # Уточнение по порогу покрытия

### OpenAPI спецификации // Раздел OpenAPI

```bash
# users-api: спецификация доступна по HTTP
curl -s http://localhost:8082/openapi.yaml # Получаем YAML спецификацию users-api

# admin-api: спецификация доступна по HTTP
curl -s http://localhost:8083/openapi.yaml # Получаем YAML спецификацию admin-api
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
export USERS_API_URL="http://localhost:8082" # Базовый URL users-api (для api-gateway)
export ADMIN_API_URL="http://localhost:8083" # Базовый URL admin-api (для api-gateway)
export LG_MOCK="true" # Мок для LG облака
export SBER_MOCK="true" # Мок для Сбербанка
export ORANGEDATA_MOCK="true" # Мок для OrangeData
export ARDUINO_MOCK="true" # Мок для Arduino дозатора
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

### Типовые ошибки и решения // Раздел troubleshooting

```text
- Отсутствует sbt: установите sbt и JDK 21. На Windows используйте пакетный установщик или скачайте с сайта sbt.
- Docker недоступен в тестах Testcontainers: тест пропустит выполнение, но для полноценного прогона запустите Docker Desktop и повторите.
- Порты заняты: измените APP_HTTP_PORT в переменных окружения или остановите конфликтующие процессы.
- Недоступна БД: убедитесь, что сервис postgres в docker compose в состоянии healthy; проверьте переменные APP_DB_*.
```

### TODO / Открытые вопросы // Раздел вопросов

- Таймзона и время ежедневной рассылки отчётов. // Нужна фиксация TZ и расписания
- Поля и формат чеков OrangeData. // Требуется согласование формата
- Тип подключения Arduino и калибровка. // Определить транспорт и коэффициенты
- Полный список программ и цены. // Заполнить каталог программ


