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

