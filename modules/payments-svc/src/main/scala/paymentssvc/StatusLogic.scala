// Пакет платёжного сервиса
package paymentssvc // Объявляем пакет

// Импорт доменных типов
import paymentssvc.* // Импортируем статусы и события

// Логика переходов статусов платежа
object StatusLogic: // Объявляем объект с логикой
  // Функция перехода по текущему статусу и событию
  def next(current: PaymentStatus, event: PaymentEvent): PaymentStatus = // Сигнатура функции перехода
    (current, event) match // Сопоставляем по парам
      case (PaymentStatus.NEW, PaymentEvent.Init) => PaymentStatus.PENDING // NEW + Init -> PENDING
      case (PaymentStatus.PENDING, PaymentEvent.ProviderPending) => PaymentStatus.PENDING // PENDING + pending -> PENDING
      case (PaymentStatus.PENDING, PaymentEvent.ProviderPaid) => PaymentStatus.PAID // PENDING + paid -> PAID
      case (PaymentStatus.PENDING, PaymentEvent.ProviderFailed) => PaymentStatus.FAILED // PENDING + failed -> FAILED
      case (PaymentStatus.PENDING, PaymentEvent.ProviderExpired) => PaymentStatus.EXPIRED // PENDING + expired -> EXPIRED
      case (s, _) => s // Любые другие события не меняют статус

