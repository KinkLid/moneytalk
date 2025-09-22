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

// Кодеки Circe
object JsonCodecs: // Объявляем объект кодеков
  given Encoder[MachineDto] = deriveEncoder[MachineDto] // Кодек машины
  given Decoder[MachineDto] = deriveDecoder[MachineDto] // Декодер машины
  given Encoder[ProgramDto] = deriveEncoder[ProgramDto] // Кодек программы
  given Decoder[ProgramDto] = deriveDecoder[ProgramDto] // Декодер программы
  given Encoder[MachinesResponse] = deriveEncoder[MachinesResponse] // Кодек ответа
  given Decoder[MachinesResponse] = deriveDecoder[MachinesResponse] // Декодер ответа

