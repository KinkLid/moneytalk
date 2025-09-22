// Пакет платёжного сервиса
package paymentssvc // Объявляем пакет

// Импорт ZIO
import zio.* // Импорт ядра ZIO

// Контракт клиента Сбербанка
trait SberClient: // Объявляем трейд клиента
  def initPayment(orderId: String, amountCents: Int): Task[String] // Инициация платежа, возвращает paymentId
  def getStatus(paymentId: String): Task[PaymentStatus] // Запрос статуса платежа
  def verifyCallbackSignature(payload: String, signature: String): Task[Boolean] // Проверка подписи колбэка

// Мок-реализация клиента Сбера
final class MockSberClient extends SberClient: // Мок-клиент Сбера
  def initPayment(orderId: String, amountCents: Int): Task[String] = // Инициация платежа
    ZIO.succeed(s"pm_$orderId") // Возвращаем синтетический paymentId
  def getStatus(paymentId: String): Task[PaymentStatus] = // Статус платежа
    ZIO.succeed(PaymentStatus.PAID) // Возвращаем PAID для упрощения
  def verifyCallbackSignature(payload: String, signature: String): Task[Boolean] = // Проверка подписи
    ZIO.succeed(true) // Всегда true в мок-режиме

// Тестовый слой для мок-клиента
object MockSberClient: // Компаньон-объект
  val layer: ULayer[SberClient] = ZLayer.succeed(new MockSberClient) // Слой с мок-клиентом

