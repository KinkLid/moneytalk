// Пакет для users-api валидаторов
package usersapi // Объявляем пакет

// Импортируем модели
import usersapi.* // Импорт DTO

// Результат валидации
enum ValidationError: // Перечисление ошибок
  case EmptyId // Пустой идентификатор
  case NonPositivePrice // Неположительная цена
  case NonPositiveDuration // Неположительная длительность

// Валидаторы DTO
object Validators: // Объявляем объект валидаторов
  // Валидация машины
  def validateMachine(m: MachineDto): Either[ValidationError, MachineDto] = // Сигнатура
    if (m.id.trim.isEmpty) Left(ValidationError.EmptyId) // Проверяем пустой id
    else Right(m) // Иначе валиден

  // Валидация программы
  def validateProgram(p: ProgramDto): Either[ValidationError, ProgramDto] = // Сигнатура
    if (p.priceCents <= 0) Left(ValidationError.NonPositivePrice) // Цена должна быть > 0
    else if (p.durationMinutes <= 0) Left(ValidationError.NonPositiveDuration) // Длительность > 0
    else Right(p) // Иначе валидно

