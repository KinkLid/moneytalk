ThisBuild / version := "0.1.0-SNAPSHOT"
// Импортируем ключи scoverage для настройки покрытия
import scoverage.ScoverageKeys._ // Импорт ключей покрытия тестами

// Включаем поддержку Scala 3 и определяем общие настройки для всех модулей
ThisBuild / scalaVersion := "3.3.6" // Устанавливаем версию Scala 3 для всех подпроектов

// Включаем строгие опции компилятора для качества кода
ThisBuild / scalacOptions ++= Seq( // Добавляем набор параметров компилятора
  "-deprecation", // Показывать предупреждения об устаревших API
  "-feature", // Включить предупреждения о функциях языка, требующих импорта
  "-unchecked", // Включить дополнительные проверки времени компиляции
  "-Wvalue-discard" // Предупреждать о игнорировании результатов выражений
) // Завершаем список опций

// Настраиваем порог покрытия кода для scoverage
ThisBuild / scoverage.ScoverageKeys.coverageMinimumStmtTotal := 80.0 // Минимально допустимое покрытие по операторам
ThisBuild / scoverage.ScoverageKeys.coverageFailOnMinimum := true // Падать сборке, если покрытие ниже порога
ThisBuild / scoverage.ScoverageKeys.coverageHighlighting := true // Включить подсветку покрытия

// Общие зависимости, используемые многими модулями
lazy val zioVersion     = "2.1.9" // Версия ZIO ядра
lazy val http4sVersion  = "0.23.26" // Версия http4s для HTTP сервера/клиента
lazy val tapirVersion   = "1.10.8" // Версия Tapir для OpenAPI/эндпоинтов
lazy val circeVersion   = "0.14.10" // Версия Circe для JSON
lazy val quillVersion   = "4.8.5" // Версия Quill для доступа к БД
lazy val flywayVersion  = "10.17.0" // Версия Flyway для миграций
lazy val zioCfgVersion  = "4.0.2" // Версия zio-config
lazy val logbackVersion = "1.5.8" // Версия logback для локального логирования
lazy val testcVersion   = "0.41.4" // Версия Testcontainers Scala

// Общие настройки для всех подпроектов
lazy val commonSettings = Seq( // Определяем общие настройки
  organization := "com.laundry", // Указываем организацию проекта
  Test / fork := true, // Форкать JVM для тестов для изоляции
  Test / parallelExecution := false, // Отключаем параллелизм тестов для стабильности
  libraryDependencies ++= Seq( // Общие зависимости
    "dev.zio" %% "zio" % zioVersion, // ZIO ядро
    "dev.zio" %% "zio-streams" % zioVersion, // ZIO Streams
    "io.circe" %% "circe-core" % circeVersion, // Библиотека Circe core
    "io.circe" %% "circe-generic" % circeVersion, // Автодеривация кодеков Circe
    "io.circe" %% "circe-parser" % circeVersion, // Парсер JSON Circe
    "dev.zio" %% "zio-interop-cats" % "23.1.0.1", // Интероп ZIO и Cats Effect для http4s
    "dev.zio" %% "zio-config" % zioCfgVersion, // zio-config ядро
    "dev.zio" %% "zio-config-magnolia" % zioCfgVersion, // Автоматическое деривация конфигов
    "dev.zio" %% "zio-config-typesafe" % zioCfgVersion, // Поддержка HOCON/ENV
    "ch.qos.logback" % "logback-classic" % logbackVersion % Runtime, // Локальный логгер
    "dev.zio" %% "zio-logging" % "2.3.2", // Логирование на ZIO
    "dev.zio" %% "zio-logging-slf4j" % "2.3.2", // Мост SLF4J
    "io.getquill" %% "quill-jdbc-zio" % quillVersion, // Доступ к PostgreSQL через Quill ZIO
    "org.flywaydb" % "flyway-core" % flywayVersion, // Миграции Flyway
    "org.postgresql" % "postgresql" % "42.7.4", // JDBC драйвер PostgreSQL
    "org.typelevel" %% "cats-core" % "2.12.0", // Библиотека Cats (утилиты)
    "org.typelevel" %% "cats-effect" % "3.5.4", // Cats Effect (для http4s)
    "org.http4s" %% "http4s-ember-server" % http4sVersion, // Легкий сервер http4s
    "org.http4s" %% "http4s-ember-client" % http4sVersion, // Клиент http4s
    "org.http4s" %% "http4s-circe" % http4sVersion, // Интеграция с Circe
    "org.http4s" %% "http4s-dsl" % http4sVersion, // DSL для роутинга
    "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion, // Базовые типы Tapir
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion, // Сервер http4s для Tapir
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion, // JSON через Circe для Tapir
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion, // Генерация OpenAPI (рантайм)
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion, // Экспорт OpenAPI в YAML
    "dev.zio" %% "zio-test" % zioVersion % Test, // ZIO Test ядро
    "dev.zio" %% "zio-test-sbt" % zioVersion % Test, // Интеграция с sbt
    "com.dimafeng" %% "testcontainers-scala-postgresql" % testcVersion % Test, // Testcontainers Postgres
    "com.dimafeng" %% "testcontainers-scala-scalatest" % testcVersion % Test // Утилиты для тестов
  ) // Конец списка зависимостей
) // Конец общих настроек

// Определяем корневой агрегирующий проект без собственных исходников
lazy val root = (project in file(".")) // Проект в корне репозитория
  .aggregate( // Агрегируем подпроекты для удобного запуска задач
    common,
    migrations,
    apiGateway,
    usersApi,
    adminApi,
    machinesSvc,
    lgAdapter,
    doserGateway,
    paymentsSvc,
    fiscalSvc,
    reportingSvc,
    emailSvc
  ) // Завершаем список агрегатов
  .settings( // Настройки корневого проекта
    name := "laundry-automation", // Имя корневого проекта
    publish / skip := true // Запрещаем публикацию артефактов корневого проекта
  ) // Завершение настроек

// Модуль common с общими типами и утилитами
lazy val common = (project in file("modules/common")) // Путь к модулю common
  .settings( // Добавляем общие и модульные настройки
    commonSettings, // Подключаем общие настройки
    name := "common" // Имя модуля
  ) // Завершение настроек

// Модуль migrations для Flyway SQL миграций
lazy val migrations = (project in file("modules/migrations")) // Путь к модулю миграций
  .settings( // Настройки для миграций
    commonSettings, // Общие настройки
    name := "migrations", // Имя модуля
    publish / skip := true // Не публиковать артефакты
  ) // Завершение настроек

// Сервис api-gateway
lazy val apiGateway = (project in file("modules/api-gateway")) // Путь к модулю api-gateway
  .dependsOn(common) // Зависит от common
  .settings(commonSettings, name := "api-gateway") // Имя и общие настройки

// Сервис users-api
lazy val usersApi = (project in file("modules/users-api")) // Путь к модулю users-api
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "users-api") // Имя и настройки

// Сервис admin-api
lazy val adminApi = (project in file("modules/admin-api")) // Путь к модулю admin-api
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "admin-api") // Имя и настройки

// Сервис machines-svc
lazy val machinesSvc = (project in file("modules/machines-svc")) // Путь к модулю machines-svc
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "machines-svc") // Имя и настройки

// Адаптер LG облака
lazy val lgAdapter = (project in file("modules/lg-adapter")) // Путь к модулю lg-adapter
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "lg-adapter") // Имя и настройки

// Шлюз дозатора (Arduino)
lazy val doserGateway = (project in file("modules/doser-gateway")) // Путь к модулю doser-gateway
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "doser-gateway") // Имя и настройки

// Платежный сервис
lazy val paymentsSvc = (project in file("modules/payments-svc")) // Путь к модулю payments-svc
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "payments-svc") // Имя и настройки

// Фискальный сервис (OrangeData)
lazy val fiscalSvc = (project in file("modules/fiscal-svc")) // Путь к модулю fiscal-svc
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "fiscal-svc") // Имя и настройки

// Сервис отчетности
lazy val reportingSvc = (project in file("modules/reporting-svc")) // Путь к модулю reporting-svc
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "reporting-svc") // Имя и настройки

// Сервис отправки почты
lazy val emailSvc = (project in file("modules/email-svc")) // Путь к модулю email-svc
  .dependsOn(common) // Зависимость на common
  .settings(commonSettings, name := "email-svc") // Имя и настройки

// Удалено дублирующееся определение корневого проекта ниже по файлу
