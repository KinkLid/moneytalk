// Пакет платежного сервиса моделей
package paymentssvc // Объявляем пакет

// Импорт Circe
import io.circe.* // Базовые типы Circe
import io.circe.generic.semiauto.* // Полуавтоматическая деривация

// Запрос колбэка провайдера
final case class PaymentCallbackRequest(paymentId: String, status: String) // Поля колбэка

// Ответ статуса платежа
final case class PaymentStatusResponse(paymentId: String, status: String) // Поля ответа статуса

// Кодеки JSON
object JsonCodecs: // Объявляем объект кодеков
  given Encoder[PaymentCallbackRequest] = deriveEncoder[PaymentCallbackRequest] // Кодек запроса
  given Decoder[PaymentCallbackRequest] = deriveDecoder[PaymentCallbackRequest] // Декодер запроса
  given Encoder[PaymentStatusResponse] = deriveEncoder[PaymentStatusResponse] // Кодек ответа
  given Decoder[PaymentStatusResponse] = deriveDecoder[PaymentStatusResponse] // Декодер ответа


