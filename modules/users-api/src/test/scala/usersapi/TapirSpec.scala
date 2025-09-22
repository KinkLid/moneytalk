// Пакет тестов users-api
package usersapi // Объявляем пакет

// Импорт ZIO Test
import zio.* // Импорт ядра ZIO
import zio.test.* // Импорт ZIO Test
import zio.test.Assertion.* // Импорт утверждений

// Импорт Tapir OpenAPI
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter // Интерпретатор в OpenAPI
import sttp.apispec.openapi.OpenAPI // Тип OpenAPI

// Given: Tapir эндпоинты
object TapirSpec extends ZIOSpecDefault: // Объявляем набор тестов
  // When/Then: генерируем OpenAPI и проверяем наличие путей
  def spec = suite("Users Tapir OpenAPI") ( // Название набора
    test("Given endpoints When toOpenAPI Then has /machines path") { // Описание теста
      val docs: OpenAPI = OpenAPIDocsInterpreter().toOpenAPI(List(TapirEndpoints.machinesEndpoint), "users-api", "0.1") // Генерируем OpenAPI
      assertTrue(docs.toString.contains("/machines")) // Проверяем наличие пути по строковому представлению
    } // Конец теста
  ) // Конец набора

