// Пакет для machines-svc
package machinessvc // Объявляем пакет

// Модель машины
final case class Machine(id: String, isActive: Boolean) // Идентификатор и флаг активности

// Модель программы
final case class Program(code: String, name: String, durationMinutes: Int, priceCents: Int) // Свойства программы

// Ошибки домена
enum MachineError: // Перечисление ошибок
  case MachineInactive // Машина недоступна
  case ProgramNotFound // Программа не найдена

// Сервис выбора программы и проверки доступности
object MachineService: // Объявляем объект сервиса
  // Проверяем доступность машины
  def ensureAvailable(machine: Machine): Either[MachineError, Unit] = // Возвращаем Either с ошибкой или Unit
    if (machine.isActive) Right(()) else Left(MachineError.MachineInactive) // Активна? тогда ок, иначе ошибка

  // Находим программу по коду
  def findProgram(programs: List[Program], code: String): Either[MachineError, Program] = // Поиск по коду
    programs.find(_.code == code).toRight(MachineError.ProgramNotFound) // Возвращаем найденную или ошибку

