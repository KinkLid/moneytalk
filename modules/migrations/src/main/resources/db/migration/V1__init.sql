-- Создаём схему данных для системы прачечной
CREATE SCHEMA IF NOT EXISTS laundry; -- Создаём схему laundry, если не существует

-- Устанавливаем схему по умолчанию
SET search_path TO laundry; -- Устанавливаем контекст схемы laundry

-- Таблица пользователей
CREATE TABLE IF NOT EXISTS users ( -- Создаём таблицу users, если не существует
  id UUID PRIMARY KEY, -- Первичный ключ пользователя
  email TEXT NOT NULL UNIQUE, -- Email пользователя, уникальный
  role TEXT NOT NULL, -- Роль пользователя (user/admin)
  created_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время создания
); -- Завершаем создание таблицы users

-- Таблица машин
CREATE TABLE IF NOT EXISTS machines ( -- Таблица для стиральных/сушильных машин
  id UUID PRIMARY KEY, -- Идентификатор машины
  vendor TEXT NOT NULL, -- Производитель (например, LG)
  model TEXT NOT NULL, -- Модель
  external_id TEXT NOT NULL UNIQUE, -- Идентификатор во внешней системе (облако)
  is_active BOOLEAN NOT NULL DEFAULT true, -- Флаг активности
  created_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время создания
); -- Конец таблицы machines

-- Таблица программ
CREATE TABLE IF NOT EXISTS programs ( -- Таблица программ стирки/сушки
  id UUID PRIMARY KEY, -- Идентификатор программы
  machine_id UUID NOT NULL REFERENCES machines(id) ON DELETE CASCADE, -- Связь с машиной
  code TEXT NOT NULL, -- Код программы (например, QUICK30)
  name TEXT NOT NULL, -- Человекочитаемое имя программы
  price_cents INTEGER NOT NULL, -- Цена в копейках
  duration_minutes INTEGER NOT NULL, -- Длительность в минутах
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), -- Время создания
  UNIQUE(machine_id, code) -- Уникальность кода в рамках машины
); -- Конец таблицы programs

-- Таблица заказов
CREATE TABLE IF NOT EXISTS orders ( -- Таблица заказов пользователей
  id UUID PRIMARY KEY, -- Идентификатор заказа
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE RESTRICT, -- Пользователь
  machine_id UUID NOT NULL REFERENCES machines(id) ON DELETE RESTRICT, -- Машина
  program_id UUID NOT NULL REFERENCES programs(id) ON DELETE RESTRICT, -- Программа
  status TEXT NOT NULL, -- Статус заказа (NEW/PENDING/PAID/FAILED/STARTED/DONE)
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), -- Время создания
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время обновления
); -- Конец таблицы orders

-- Таблица платежей
CREATE TABLE IF NOT EXISTS payments ( -- Таблица платежей
  id UUID PRIMARY KEY, -- Идентификатор платежа
  order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE, -- Связь с заказом
  provider TEXT NOT NULL, -- Провайдер (SBER)
  status TEXT NOT NULL, -- Статус (NEW/PENDING/PAID/FAILED/EXPIRED)
  amount_cents INTEGER NOT NULL, -- Сумма в копейках
  provider_payment_id TEXT, -- Идентификатор в провайдере
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), -- Время создания
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время обновления
); -- Конец таблицы payments

-- Таблица чеков (фискализация)
CREATE TABLE IF NOT EXISTS receipts ( -- Таблица чеков OrangeData
  id UUID PRIMARY KEY, -- Идентификатор чека
  order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE, -- Связь с заказом
  status TEXT NOT NULL, -- Статус (NEW/SENT/DONE/FAILED)
  payload_json JSONB NOT NULL, -- Тело запроса чека
  provider_receipt_id TEXT, -- Идентификатор в OrangeData
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), -- Время создания
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время обновления
); -- Конец таблицы receipts

-- Таблица логов дозирования
CREATE TABLE IF NOT EXISTS dosing_logs ( -- Логи команд дозатора
  id UUID PRIMARY KEY, -- Идентификатор записи
  order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE, -- Связь с заказом
  channel TEXT NOT NULL, -- Канал дозирования (например, D)
  ml INTEGER NOT NULL, -- Объём в миллилитрах
  status TEXT NOT NULL, -- Статус выполнения (OK/ERR/TIMEOUT)
  created_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время создания
); -- Конец таблицы dosing_logs

-- Таблица отчётов
CREATE TABLE IF NOT EXISTS reports ( -- Ежедневные агрегаты
  id UUID PRIMARY KEY, -- Идентификатор отчёта
  report_date DATE NOT NULL, -- Дата отчёта
  payload_json JSONB NOT NULL, -- Данные отчёта
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), -- Время создания
  UNIQUE(report_date) -- Один отчёт на дату
); -- Конец таблицы reports

-- Outbox для асинхронных задач
CREATE TABLE IF NOT EXISTS outbox ( -- Таблица outbox для событий
  id UUID PRIMARY KEY, -- Идентификатор события
  event_type TEXT NOT NULL, -- Тип события (EMAIL/FISCAL/ORDER_POST_PAID)
  payload_json JSONB NOT NULL, -- Полезная нагрузка события
  attempts INTEGER NOT NULL DEFAULT 0, -- Количество попыток обработки
  status TEXT NOT NULL DEFAULT 'NEW', -- Статус (NEW/PROCESSING/DONE/DLQ)
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), -- Время создания
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время обновления
); -- Конец таблицы outbox

-- Очередь писем для email-сервиса
CREATE TABLE IF NOT EXISTS email_outbox ( -- Таблица для писем
  id UUID PRIMARY KEY, -- Идентификатор письма
  to_email TEXT NOT NULL, -- Получатель
  subject TEXT NOT NULL, -- Тема письма
  body TEXT NOT NULL, -- Тело письма
  status TEXT NOT NULL DEFAULT 'NEW', -- Статус (NEW/SENT/FAILED/DLQ)
  attempts INTEGER NOT NULL DEFAULT 0, -- Количество попыток
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(), -- Время создания
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now() -- Время обновления
); -- Конец таблицы email_outbox

-- Индексы для ускорения запросов
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders(user_id); -- Индекс по пользователю
CREATE INDEX IF NOT EXISTS idx_orders_machine ON orders(machine_id); -- Индекс по машине
CREATE INDEX IF NOT EXISTS idx_payments_order ON payments(order_id); -- Индекс по заказу
CREATE INDEX IF NOT EXISTS idx_receipts_order ON receipts(order_id); -- Индекс по заказу для чеков
CREATE INDEX IF NOT EXISTS idx_outbox_status ON outbox(status); -- Индекс по статусу outbox
CREATE INDEX IF NOT EXISTS idx_email_outbox_status ON email_outbox(status); -- Индекс по статусу писем

