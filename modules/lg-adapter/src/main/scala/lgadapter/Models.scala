// Пакет для lg-adapter моделей
package lgadapter // Объявляем пакет

// Импорт Circe для JSON
import io.circe.* // Базовые типы Circe
import io.circe.generic.semiauto.* // Полуавтоматическая деривация кодеков

// Запрос запуска цикла
final case class StartCycleRequest(machineId: String, programCode: String) // Поля запроса запуска

// Ответ статуса машины
final case class StatusResponse(machineId: String, status: String) // Поля ответа статуса

// Кодеки JSON
object JsonCodecs: // Объявляем объект кодеков
  given Encoder[StartCycleRequest] = deriveEncoder[StartCycleRequest] // Кодек запроса запуска
  given Decoder[StartCycleRequest] = deriveDecoder[StartCycleRequest] // Декодер запроса запуска
  given Encoder[StatusResponse] = deriveEncoder[StatusResponse] // Кодек ответа статуса
  given Decoder[StatusResponse] = deriveDecoder[StatusResponse] // Декодер ответа статуса


