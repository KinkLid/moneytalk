file:///C:/coc/moneytalk/modules/admin-api/src/main/scala/adminapi/HttpServer.scala
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
uri: file:///C:/coc/moneytalk/modules/admin-api/src/main/scala/adminapi/HttpServer.scala
text:
```scala
// Пакет для admin-api
package adminapi // Объявляем пакет

// Импорты http4s и ZIO
import cats.effect.* // Импорт для интеграции с http4s (Cats Effect)
import org.http4s.* // Базовые типы http4s
import org.http4s.dsl.io.* // DSL для роутинга на IO
import org.http4s.ember.server.* // Лёгкий сервер http4s
import com.comcast.ip4s.* // Типы адресов/портов
import zio.ZIO // Импортируем только тип ZIO
import zio.interop.catz.* // Интероп ZIO <-> Cats Effect
import org.http4s.circe.* // Поддержка Circe
import io.circe.syntax.* // Синтаксис .asJson
import adminapi.JsonCodecs.given // Имплиситы кодеков
import adminapi.* // Модели
import sttp.apispec.openapi.OpenAPI // Тип OpenAPI
import sttp.tapir.* // Базовые типы Tapir
import sttp.tapir.apispec.openapi.Info // Метаданные
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter // Генератор OpenAPI
import sttp.tapir.openapi.circe.yaml.* // Экспорт YAML

// Простой HTTP-сервер с эндпоинтом здоровья
object HttpServer: // Объявляем объект сервера
  // Роуты приложения
  private val routes: HttpRoutes[cats.effect.IO] = HttpRoutes.of[cats.effect.IO] { // Определяем маршруты
    case GET -> Root / "health" => Ok("OK") // Возвращаем 200 OK на /health
    case GET -> Root / "inventory" => // Эндпоинт инвентаря
      val resp = InventoryResponse(List(InventoryItem("m1", 500, 200))) // Заглушка данных
      Ok(resp.asJson) // Возвращаем JSON
    case req @ POST -> Root / "machines" / machineId / "start" => // Принудительный старт
      req.asJsonDecode[ForceStartRequest].flatMap { _ => // Декодируем тело
        Ok(CommandResponse(true, s"started ${machineId}").asJson) // Возвращаем успех
      }
    case POST -> Root / "machines" / machineId / "stop" => // Принудительная остановка
      Ok(CommandResponse(true, s"stopped ${machineId}").asJson) // Возвращаем успех
    case req @ POST -> Root / "dose" => // Ручное дозирование
      req.asJsonDecode[AdminDoseRequest].flatMap { d => // Декодируем
        Ok(CommandResponse(true, s"dosed ${d.channel}:${d.ml}").asJson) // Возвращаем успех
      }
    case GET -> Root / "reports" / reportDate => // Получение отчёта
      Ok(ReportResponse(reportDate, sent = true).asJson) // Возвращаем заглушку
    case GET -> Root / "openapi.yaml" => // Спецификация OpenAPI
      val endpoints = List( // Список эндпоинтов
        TapirEndpoints.inventoryEndpoint, // /inventory
        TapirEndpoints.forceStartEndpoint, // /machines/{id}/start
        TapirEndpoints.forceStopEndpoint, // /machines/{id}/stop
        TapirEndpoints.adminDoseEndpoint, // /dose
        TapirEndpoints.reportEndpoint // /reports/{date}
      ) // Конец списка
      val docs = OpenAPIDocsInterpreter().toOpenAPI(endpoints, Info("admin-api", "0.1.0")) // Генерация OpenAPI
      Ok(docs.toYaml).map(_.withContentType(`Content-Type`(MediaType.text.yaml))) // Отдаём YAML
  } // Завершаем определение роутов

  // Запуск сервера как ZIO-эффект
  def serve(port: Int): ZIO[Any, Throwable, Unit] = // Определяем метод запуска с портом
    ZIO.runtime[Any].flatMap { _ => // Получаем рантайм ZIO (не используем явно)
      val httpApp = routes.orNotFound // Превращаем маршруты в приложение
      val io = EmberServerBuilder.default[cats.effect.IO] // Создаём конфигурацию сервера
        .withHost(ipv4"0.0.0.0") // Слушаем на всех интерфейсах
        .withPort(Port.fromInt(port).get) // Указываем порт
        .withHttpApp(httpApp) // Устанавливаем приложение
        .build // Строим ресурс сервера
        .useForever // Держим сервер бесконечно
      ZIO.attempt { // Оборачиваем выполнение в ZIO
        import cats.effect.unsafe.implicits.global // Импортируем глобальный рантайм CE
        io.unsafeRunAndForget() // Запускаем неблокирующе
      } *> ZIO.never // Никогда не завершаем эффект
    } // Завершаем создание эффекта

  // Публичный httpApp для тестов
  def httpApp: org.http4s.HttpApp[cats.effect.IO] = routes.orNotFound // Экспортируем HttpApp


```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2609)
	dotty.tools.dotc.core.SymDenotations$SymDenotation.isSelfSym(SymDenotations.scala:715)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:330)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.fold$1(Trees.scala:1636)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.apply(Trees.scala:1638)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1669)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1677)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.fold$1(Trees.scala:1636)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.apply(Trees.scala:1638)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1675)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:457)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$13(ExtractSemanticDB.scala:391)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:386)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1720)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:354)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$11(ExtractSemanticDB.scala:377)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:377)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.apply(Trees.scala:1770)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1728)
	dotty.tools.dotc.ast.Trees$Instance$TreeAccumulator.foldOver(Trees.scala:1642)
	dotty.tools.dotc.ast.Trees$Instance$TreeTraverser.traverseChildren(Trees.scala:1771)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:351)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse$$anonfun$1(ExtractSemanticDB.scala:315)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:334)
	dotty.tools.dotc.semanticdb.ExtractSemanticDB$Extractor.traverse(ExtractSemanticDB.scala:315)
	dotty.tools.pc.SemanticdbTextDocumentProvider.textDocument(SemanticdbTextDocumentProvider.scala:36)
	dotty.tools.pc.ScalaPresentationCompiler.semanticdbTextDocument$$anonfun$1(ScalaPresentationCompiler.scala:242)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner