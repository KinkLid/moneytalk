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

  // Эндпоинт создания заказа
  val createOrderEndpoint: PublicEndpoint[CreateOrderRequest, Unit, CreateOrderResponse, Any] = // Описание
    endpoint.post // POST метод
      .in("orders") // Путь /orders
      .in(jsonBody[CreateOrderRequest]) // Входной JSON
      .out(jsonBody[CreateOrderResponse]) // Выходной JSON

  // Эндпоинт инициации платежа
  val initPaymentEndpoint: PublicEndpoint[PaymentInitRequest, Unit, PaymentInitResponse, Any] = // Описание
    endpoint.post // POST метод
      .in("payments" / "init") // Путь /payments/init
      .in(jsonBody[PaymentInitRequest]) // Входной JSON
      .out(jsonBody[PaymentInitResponse]) // Выходной JSON

  // Эндпоинт статуса заказа
  val orderStatusEndpoint: PublicEndpoint[String, Unit, OrderStatusResponse, Any] = // Описание
    endpoint.get // GET метод
      .in("orders" / path[String]("orderId")) // Путь /orders/{orderId}
      .out(jsonBody[OrderStatusResponse]) // Выходной JSON

