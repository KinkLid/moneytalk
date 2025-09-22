// Пакет платёжного сервиса
package paymentssvc // Объявляем пакет

// Определяем доменные типы платежей
enum PaymentStatus: // Перечисление статусов платежа
  case NEW // Новый платёж
  case PENDING // Ожидает подтверждения
  case PAID // Оплачен
  case FAILED // Ошибка
  case EXPIRED // Истёк срок

// Команда/событие, влияющее на статус
enum PaymentEvent: // Перечисление событий
  case Init // Инициация платежа
  case ProviderPending // Провайдер сообщил ожидание
  case ProviderPaid // Провайдер подтвердил оплату
  case ProviderFailed // Провайдер сообщил ошибку
  case ProviderExpired // Провайдер сообщил истечение

