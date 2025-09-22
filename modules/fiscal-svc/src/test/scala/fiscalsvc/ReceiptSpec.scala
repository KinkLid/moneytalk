// Пакет тестов fiscal-svc
package fiscalsvc // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: утилита сборки payload для чека
object ReceiptSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем сумму и структуру полей
  def spec = suite("Receipt payload builder") ( // Название сьюта
    test("Given items When buildPayload Then totalCents is sum") { // Проверяем сумму
      val items = List(ReceiptItem("Wash", 1000, 1), ReceiptItem("Dry", 500, 2)) // Две позиции
      val payload = Receipt.buildPayload("ord1", items, "a@b.c") // Строим payload
      assertTrue(payload("totalCents").asInstanceOf[Int] == 2000) // 1000 + 500*2 = 2000
    }, // Конец теста
    test("Given email When buildPayload Then includes email") { // Проверяем email
      val items = Nil // Пустые позиции
      val payload = Receipt.buildPayload("ord2", items, "u@x.y") // Строим payload
      assertTrue(payload("customerEmail") == "u@x.y") // Должен содержать email
    } // Конец теста
  ) // Конец сьюта

// Пакет тестов fiscal-svc
package fiscalsvc // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Given: список позиций чека
object ReceiptSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: проверяем генерацию суммы и полей
  def spec = suite("Receipt builder") ( // Название набора
    test("Given items When buildPayload Then totalCents is sum of items") { // Проверка итога
      val items = List(ReceiptItem("Wash", 15000, 1), ReceiptItem("Dry", 5000, 2)) // Две позиции
      val payload = Receipt.buildPayload("ord-1", items, "a@b.c") // Формируем payload
      assertTrue(payload("totalCents") == 15000 + 2 * 5000) // Проверяем сумму
    }, // Конец теста
    test("Given items When buildPayload Then items size correct") { // Проверка количества позиций
      val items = List(ReceiptItem("Wash", 15000, 1), ReceiptItem("Dry", 5000, 2)) // Две позиции
      val payload = Receipt.buildPayload("ord-1", items, "a@b.c") // Формируем payload
      val arr = payload("items").asInstanceOf[List[Map[String, Any]]] // Приводим к списку карт
      assertTrue(arr.size == 2) // Должно быть 2
    } // Конец теста
  ) // Конец набора

