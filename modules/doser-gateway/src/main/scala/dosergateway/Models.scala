// Пакет для doser-gateway моделей
package dosergateway // Объявляем пакет

// Импорт Circe
import io.circe.* // Базовые типы Circe
import io.circe.generic.semiauto.* // Полуавтоматическая деривация

// Запрос дозирования
final case class DoseRequest(channel: String, ml: Int) // Поля запроса дозирования

// Ответ дозирования
final case class DoseResponse(status: String, code: Option[String]) // Поля ответа дозирования

// Кодеки JSON
object JsonCodecs: // Объявляем объект кодеков
  given Encoder[DoseRequest] = deriveEncoder[DoseRequest] // Кодек запроса
  given Decoder[DoseRequest] = deriveDecoder[DoseRequest] // Декодер запроса
  given Encoder[DoseResponse] = deriveEncoder[DoseResponse] // Кодек ответа
  given Decoder[DoseResponse] = deriveDecoder[DoseResponse] // Декодер ответа


