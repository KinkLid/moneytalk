// Пакет тестов admin-api
package adminapi // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Импорт Tapir OpenAPI
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter // Интерпретатор OpenAPI
import sttp.apispec.openapi.OpenAPI // Тип OpenAPI

// Given: Tapir эндпоинты
object TapirSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: генерируем OpenAPI и проверяем наличие путей
  def spec = suite("Admin Tapir OpenAPI") ( // Название набора
    test("Given endpoints When toOpenAPI Then has /inventory path") { // Описание теста
      val docs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(List(TapirEndpoints.inventoryEndpoint), "admin-api", "0.1") // Генерируем OpenAPI
      assertTrue(docs.toString.contains("/inventory")) // Проверяем наличие пути по строковому представлению
    } // Конец теста
  ) // Конец набора

