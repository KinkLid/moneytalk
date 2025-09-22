// Пакет для users-api Tapir-эндпоинтов
package usersapi // Объявляем пакет

// Импорты Tapir и Circe
import sttp.tapir.* // Базовые типы Tapir
import sttp.tapir.CodecFormat.* // Форматы кодеков
import sttp.tapir.json.circe.* // Поддержка Circe
import sttp.tapir.generic.auto.* // Автодеривация Schema для case class
import usersapi.JsonCodecs.given // Имплиситы кодеков

// Описание эндпоинтов Users API
object TapirEndpoints: // Объявляем объект с эндпоинтами
  // Эндпоинт получения списка машин
  val machinesEndpoint: PublicEndpoint[Unit, Unit, MachinesResponse, Any] = // Типизированное описание
    endpoint.get // Метод GET
      .in("machines") // Путь /machines
      .out(jsonBody[MachinesResponse]) // Тело ответа JSON

