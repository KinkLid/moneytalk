// Пакет для users-api моделей
package usersapi // Объявляем пакет

// Импортируем Circe для JSON
import io.circe.* // Базовые типы Circe
import io.circe.generic.semiauto.* // Полуавтоматическая деривация кодеков

// DTO машины для отдачи наружу
final case class MachineDto(id: String, model: String, active: Boolean) // Поля машины

// DTO программы для отдачи наружу
final case class ProgramDto(code: String, name: String, priceCents: Int, durationMinutes: Int) // Поля программы

// Ответ списка машин
final case class MachinesResponse(items: List[MachineDto]) // Список машин

// Запрос на создание заказа
final case class CreateOrderRequest(machineId: String, programCode: String, userEmail: String) // Поля запроса создания заказа

// Ответ на создание заказа
final case class CreateOrderResponse(orderId: String, status: String) // Идентификатор и статус заказа

// Запрос на инициацию платежа
final case class PaymentInitRequest(orderId: String) // Идентификатор заказа для оплаты

// Ответ на инициацию платежа
final case class PaymentInitResponse(paymentId: String, status: String) // Идентификатор платежа и статус

// Ответ статуса заказа
final case class OrderStatusResponse(orderId: String, status: String) // Статус заказа

// Кодеки Circe
object JsonCodecs: // Объявляем объект кодеков
  given Encoder[MachineDto] = deriveEncoder[MachineDto] // Кодек машины
  given Decoder[MachineDto] = deriveDecoder[MachineDto] // Декодер машины
  given Encoder[ProgramDto] = deriveEncoder[ProgramDto] // Кодек программы
  given Decoder[ProgramDto] = deriveDecoder[ProgramDto] // Декодер программы
  given Encoder[MachinesResponse] = deriveEncoder[MachinesResponse] // Кодек ответа
  given Decoder[MachinesResponse] = deriveDecoder[MachinesResponse] // Декодер ответа
  given Encoder[CreateOrderRequest] = deriveEncoder[CreateOrderRequest] // Кодек запроса создания заказа
  given Decoder[CreateOrderRequest] = deriveDecoder[CreateOrderRequest] // Декодер запроса создания заказа
  given Encoder[CreateOrderResponse] = deriveEncoder[CreateOrderResponse] // Кодек ответа создания заказа
  given Decoder[CreateOrderResponse] = deriveDecoder[CreateOrderResponse] // Декодер ответа создания заказа
  given Encoder[PaymentInitRequest] = deriveEncoder[PaymentInitRequest] // Кодек запроса инициации платежа
  given Decoder[PaymentInitRequest] = deriveDecoder[PaymentInitRequest] // Декодер запроса инициации платежа
  given Encoder[PaymentInitResponse] = deriveEncoder[PaymentInitResponse] // Кодек ответа инициации платежа
  given Decoder[PaymentInitResponse] = deriveDecoder[PaymentInitResponse] // Декодер ответа инициации платежа
  given Encoder[OrderStatusResponse] = deriveEncoder[OrderStatusResponse] // Кодек статуса заказа
  given Decoder[OrderStatusResponse] = deriveDecoder[OrderStatusResponse] // Декодер статуса заказа

