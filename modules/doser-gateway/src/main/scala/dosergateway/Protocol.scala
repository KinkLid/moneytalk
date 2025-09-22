// Пакет для doser-gateway
package dosergateway // Объявляем пакет

// Протокол ответов дозатора
enum DoseResult: // Перечисление вариантов результата
  case Ok // Успех
  case Err(code: String) // Ошибка с кодом

// Построитель команд и парсер ответов
object Protocol: // Объявляем объект протокола
  // Построение команды дозирования вида DOSE:<channel>:<ml>\n
  def buildDoseCommand(channel: String, ml: Int): String = // Сигнатура функции построения
    s"DOSE:${channel}:${ml}\n" // Формируем строку команды

  // Парсинг строки ответа в результат
  def parseResponse(s: String): DoseResult = // Сигнатура функции парсинга
    val trimmed = s.trim // Убираем перевод строки/пробелы
    if trimmed == "OK" then DoseResult.Ok // Если OK, возвращаем успех
    else if trimmed.startsWith("ERR:") then // Если начинается с ERR:
      DoseResult.Err(trimmed.stripPrefix("ERR:")) // Берём код ошибки после префикса
    else DoseResult.Err("UNKNOWN") // Иначе неизвестная ошибка

