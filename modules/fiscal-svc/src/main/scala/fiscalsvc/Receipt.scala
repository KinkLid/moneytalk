// Пакет для fiscal-svc
package fiscalsvc // Объявляем пакет

// Модель позиции чека
final case class ReceiptItem(name: String, priceCents: Int, qty: Int) // Имя, цена, количество

// Утилиты формирования тела чека
object Receipt: // Объявляем объект утилит
  // Сборка JSON-подобной структуры для чека
  def buildPayload(orderId: String, items: List[ReceiptItem], email: String): Map[String, Any] = // Сигнатура функции
    val sum = items.map(i => i.priceCents * i.qty).sum // Сумма по позициям в копейках
    Map( // Возвращаем карту
      "orderId" -> orderId, // Идентификатор заказа
      "customerEmail" -> email, // Email клиента
      "totalCents" -> sum, // Общая сумма
      "items" -> items.map(i => Map("name" -> i.name, "priceCents" -> i.priceCents, "qty" -> i.qty)) // Позиции
    ) // Конец Map

