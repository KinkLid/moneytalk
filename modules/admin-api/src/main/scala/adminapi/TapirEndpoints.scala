// Пакет для admin-api Tapir-эндпоинтов
package adminapi // Объявляем пакет

// Импорты Tapir и Circe
import sttp.tapir.* // Базовые типы Tapir
import sttp.tapir.json.circe.* // Поддержка Circe
import sttp.tapir.generic.auto.* // Автодеривация Schema
import adminapi.JsonCodecs.given // Имплиситы кодеков

// Описание эндпоинтов Admin API
object TapirEndpoints: // Объявляем объект с эндпоинтами
  // Эндпоинт получения инвентаря
  val inventoryEndpoint: PublicEndpoint[Unit, Unit, InventoryResponse, Any] = // Типизированное описание
    endpoint.get // Метод GET
      .in("inventory") // Путь /inventory
      .out(jsonBody[InventoryResponse]) // Ответ JSON

  // Принудительный старт машины
  val forceStartEndpoint: PublicEndpoint[(String, ForceStartRequest), Unit, CommandResponse, Any] = // Описание
    endpoint.post // POST метод
      .in("machines" / path[String]("machineId") / "start") // Путь /machines/{id}/start
      .in(jsonBody[ForceStartRequest]) // Тело запроса
      .out(jsonBody[CommandResponse]) // Ответ

  // Принудительная остановка машины
  val forceStopEndpoint: PublicEndpoint[String, Unit, CommandResponse, Any] = // Описание
    endpoint.post // POST метод
      .in("machines" / path[String]("machineId") / "stop") // Путь /machines/{id}/stop
      .out(jsonBody[CommandResponse]) // Ответ

  // Ручное дозирование
  val adminDoseEndpoint: PublicEndpoint[AdminDoseRequest, Unit, CommandResponse, Any] = // Описание
    endpoint.post // POST
      .in("dose") // Путь /dose
      .in(jsonBody[AdminDoseRequest]) // Тело запроса
      .out(jsonBody[CommandResponse]) // Ответ

  // Получение отчёта
  val reportEndpoint: PublicEndpoint[String, Unit, ReportResponse, Any] = // Описание
    endpoint.get // GET
      .in("reports" / path[String]("reportDate")) // Путь /reports/{date}
      .out(jsonBody[ReportResponse]) // Ответ

